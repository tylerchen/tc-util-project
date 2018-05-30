/*******************************************************************************
 * Copyright (c) 2018-05-30 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.infra.util;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 提供四种线程池功能，主要目的是控制线程数量。
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-05-30
 * auto generate by qdp.
 */
public class ThreadPoolHelper {
    private static final org.iff.infra.util.Logger.Log Logger = org.iff.infra.util.Logger.get("FOSS.THREADPOOL");
    //    Java通过Executors提供四种线程池，分别为：
    //    newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
    //    newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
    //    newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
    //    newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
    private static ExecutorService cached;
    private static ExecutorService fixed;
    private static ScheduledExecutorService scheduled;
    private static ExecutorService single;

    static {
        Logger.info("ThreadPool initialize...");
        init();
        Logger.info("ThreadPool initialize finished.");
    }

    /**
     * 获得 CPU 核心数。
     *
     * @return
     */
    public static int getCpuCore() {
        int processors = Runtime.getRuntime().availableProcessors();
        return processors;
    }

    /**
     * 初始化线程池，自动调用。
     */
    public static void init() {
        int cpuCore = getCpuCore();
        int threadNum = Math.max(cpuCore * 2, 8);
        Logger.info("ThreadPool CPU core number: " + cpuCore + ", thread number: " + cpuCore + ".");
        cached = Executors.newCachedThreadPool(new BasicThreadFactory.Builder().build());
        fixed = Executors.newFixedThreadPool(threadNum, new BasicThreadFactory.Builder().build());
        scheduled = Executors.newScheduledThreadPool(threadNum, new BasicThreadFactory.Builder().build());
        single = Executors.newSingleThreadExecutor(new BasicThreadFactory.Builder().build());
        Logger.info("ThreadPool created CachedThreadPool, created FixedThreadPool, created ScheduledThreadPool, created SingleThreadExecutor.");
    }

    /**
     * 采用 Cached 线程池执行任务，线程池大小会自动增长，无限制，使用完成后会自动回收。
     *
     * @param runnable
     */
    public static void executeCached(Runnable runnable) {
        getCached().execute(PreCheckHelper.checkNotNull(runnable, "ThreadPool runnable thread is required!"));
    }

    /**
     * 采用 Fixed 线程池执行任务，线程池大小是固定的，大小为 CpuCoe*2 或 8。
     *
     * @param runnable
     */
    public static void executeFixed(Runnable runnable) {
        getFixed().execute(PreCheckHelper.checkNotNull(runnable, "ThreadPool runnable thread is required!"));
    }

    /**
     * 采用 Scheduled 线程池执行任务，线程大小是固定的，大小为 CpuCoe*2 或 8，适合执行一些定时任务。
     *
     * @param runnable
     * @param delay
     * @param timeUnit
     */
    public static void executeScheduled(Runnable runnable, long delay, TimeUnit timeUnit) {
        getScheduled().schedule(PreCheckHelper.checkNotNull(runnable, "ThreadPool runnable thread is required!"), delay, timeUnit);
    }

    /**
     * 采用 Scheduled 线程池执行任务，线程大小是固定的，大小为 CpuCoe*2 或 8，适合执行一些定时任务。
     *
     * @param runnable
     * @param initialDelay
     * @param delay
     * @param timeUnit
     */
    public static void executeScheduled(Runnable runnable, long initialDelay,
                                        long delay, TimeUnit timeUnit) {
        getScheduled().scheduleWithFixedDelay(PreCheckHelper.checkNotNull(runnable, "ThreadPool runnable thread is required!"), initialDelay, delay, timeUnit);
    }

    /**
     * 采用 Single 线程池执行任务，线程大小是固定为1，顺序执行各线程。
     *
     * @param runnable
     */
    public static void executeSingle(Runnable runnable) {
        getSingle().execute(PreCheckHelper.checkNotNull(runnable, "ThreadPool runnable thread is required!"));
    }

    public static ExecutorService getCached() {
        return cached;
    }

    public static ExecutorService getFixed() {
        return fixed;
    }

    public static ScheduledExecutorService getScheduled() {
        return scheduled;
    }

    public static ExecutorService getSingle() {
        return single;
    }
}
