/*******************************************************************************
 * Copyright (c) Aug 21, 2017 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Map;
import java.util.Random;

/**
 * <pre>
 * 由Snowflake ID变化而成，workerId由有效ip后一段组成，datacenterId由一个随机数组成，适用于局域网内ID生成。
 * Snowflake ID有64bits长，由以下三部分组成：
 *     time—42bits,精确到ms，那就意味着其可以表示长达(2^42-1)/(1000360024*365)=139.5年，另外使用者可以自己定义一个开始纪元（epoch)，然后用(当前时间-开始纪元）算出time，这表示在time这个部分在140年的时间里是不会重复的，官方文档在这里写成了41bits，应该是写错了。另外，这里用time还有一个很重要的原因，就是可以直接更具time进行排序，对于twitter这种更新频繁的应用，时间排序就显得尤为重要了。
 *     machine id—10bits,该部分其实由datacenterId和workerId两部分组成，这两部分是在配置文件中指明的。
 *         datacenterId的作用(个人看法)
 *         1.方便搭建多个生成uid的service，并保证uid不重复，比如在datacenter0将机器0，1，2组成了一个生成uid的service，而datacenter1此时也需要一个生成uid的service，从本中心获取uid显然是最快最方便的，那么它可以在自己中心搭建，只要保证datacenterId唯一。如果没有datacenterId，即用10bits，那么在搭建一个新的service前必须知道目前已经在用的id，否则不能保证生成的id唯一，比如搭建的两个uid service中都有machine id为100的机器，如果其server时间相同，那么产生相同id的情况不可避免。
 *         2.加快server启动速度。启动一台uid server时，会去检查zk同workerId目录中其他机器的情况，如其在zk上注册的id和向它请求返回的work_id是否相同，是否处同一个datacenter下，另外还会检查该server的时间与目前已有机器的平均时间误差是否在10s范围内等，这些检查是会耗费一定时间的。将一个datacenter下的机器数限制在32台(5bits)以内，在一定程度上也保证了server的启动速度。
 *         workerId是实际server机器的代号，最大到32，同一个datacenter下的workerId是不能重复的。它会被注册到zookeeper上，确保workerId未被其他机器占用，并将host:port值存入，注册成功后就可以对外提供服务了。
 *     sequence id —12bits,该id可以表示4096个数字，它是在time相同的情况下，递增该值直到为0，即一个循环结束，此时便只能等到下一个ms到来，一般情况下4096/ms的请求是不太可能出现的，所以足够使用了。
 * Snowflake ID便是通过这三部分实现了UID的产生，策略也并不复杂。
 * 核心代码就是毫秒级时间41位+机器ID 10位+毫秒内序列12位。
 * 局域网内分布式ID生成器：
 * |------time-40bit------|------matchine-id-ip1-8bit-random-6bit-----|------sequence-id-10bit-----|
 * </pre>
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 21, 2017
 */
public class SnowflakeIdExHelper {
	//开始该类生成ID的时间截，1451577600000 (2016-01-01 00:00:00 GMT) 这一时刻到当前时间所经过的毫秒数，占 41 位（还有一位是符号位，永远为 0）。
	private static final long startTime = 1451577600000L;
	//机器id所占的位数
	//IP所占位数8，只取IP最后一个数，IP范围只能是1-255
	private static final long workerIdBits = 8L;
	//数据标识id所占的位数，只占6位
	private static final long datacenterIdBits = 6L;
	//支持的最大机器id，结果是31,这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数（不信的话可以自己算一下，记住，计算机中存储一个数都是存储的补码，结果是负数要从补码得到原码）
	private static final long maxWorkerId = -1L ^ (-1L << workerIdBits);
	//支持的最大数据标识id
	private static final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	//序列在id中占的位数
	private static final long sequenceBits = 10L;
	//机器id向左移sequenceBits位
	private static final long workerIdLeftShift = sequenceBits;
	//数据标识id向左移datacenterIdLeftShift位
	private static final long datacenterIdLeftShift = workerIdBits + workerIdLeftShift;
	//时间截向左移timestampLeftShift位
	private static final long timestampLeftShift = datacenterIdBits + datacenterIdLeftShift;
	//生成序列的掩码，这里为1111 1111 1111
	private static final long sequenceMask = -1 ^ (-1 << sequenceBits);
	private long workerId;
	private long datacenterId;
	//同一个时间截内生成的序列数，初始值是0，从0开始
	private long sequence = 0L;
	//上次生成id的时间截
	private long lastTimestamp = -1L;
	//默认ID生成策略
	private static final SnowflakeIdExHelper DEFAULT = new SnowflakeIdExHelper(getIpWorkerId(),
			new Random().nextInt(Long.valueOf(maxDatacenterId).intValue()));

	/**
	 * 使用默认策略生成id。
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Aug 21, 2017
	 */
	public static long id() {
		return DEFAULT.nextId();
	}

	/**
	 * 获得IP地址最后一段。
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Aug 22, 2017
	 */
	private static long getIpWorkerId() {
		Map<String, InetAddress> address = HttpHelper.getAddress();
		for (InetAddress ia : address.values()) {
			if (ia instanceof Inet4Address && !ia.isAnyLocalAddress() && !ia.isAnyLocalAddress()
					&& !ia.isMulticastAddress()) {
				return ia.getAddress()[3];
			}
		}
		return new Random().nextInt(Long.valueOf(maxWorkerId).intValue());
	}

	public SnowflakeIdExHelper(long workerId, long datacenterId) {
		if (workerId < 0 || workerId > maxWorkerId) {
			throw new IllegalArgumentException(String
					.format("workerId[%d] is less than 0 or greater than maxWorkerId[%d].", workerId, maxWorkerId));
		}
		if (datacenterId < 0 || datacenterId > maxDatacenterId) {
			throw new IllegalArgumentException(
					String.format("datacenterId[%d] is less than 0 or greater than maxDatacenterId[%d].", datacenterId,
							maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	/**
	 * 生成id
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Aug 21, 2017
	 */
	public synchronized long nextId() {
		long timestamp = timeGen();
		if (timestamp < lastTimestamp) {
			throw new RuntimeException(String.format(
					"Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}
		//如果是同一时间生成的，则自增
		if (timestamp == lastTimestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				System.out.println("More id is required!");
				//生成下一个毫秒级的序列  
				timestamp = tilNextMillis();
				//序列从0开始  
				sequence = 0L;
			}
		} else {
			//如果发现是下一个时间单位，则自增序列回0，重新自增 
			sequence = 0L;
		}

		lastTimestamp = timestamp;

		//看本文第二部分的结构图，移位并通过或运算拼到一起组成64位的ID
		//		{
		//			String b = Long.toString((timestamp - startTime) << timestampLeftShift, 2);
		//			b = StringUtils.leftPad(b, 64, '0');
		//			System.out.println(b);
		//		}
		//		{
		//			String b = Long.toString(datacenterId << datacenterIdLeftShift, 2);
		//			b = StringUtils.leftPad(b, 64, '0');
		//			System.out.println(b);
		//		}
		//		{
		//			String b = Long.toString(workerId << workerIdLeftShift, 2);
		//			b = StringUtils.leftPad(b, 64, '0');
		//			System.out.println(b);
		//		}
		//		{
		//			String b = Long.toString(sequence, 2);
		//			b = StringUtils.leftPad(b, 64, '0');
		//			System.out.println(b);
		//		}
		return ((timestamp - startTime) << timestampLeftShift) | (datacenterId << datacenterIdLeftShift)
				| (workerId << workerIdLeftShift) | sequence;
	}

	protected long tilNextMillis() {
		long timestamp = timeGen();
		if (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	protected long timeGen() {
		return System.currentTimeMillis();
	}
}
