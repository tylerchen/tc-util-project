/*******************************************************************************
 * Copyright (c) 2018-05-29 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.infra.util.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.iff.infra.util.*;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * LogKafkaHelper
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-05-29
 * auto generate by qdp.
 */
public class LogKafkaHelper {

    private static final org.iff.infra.util.Logger.Log Logger = org.iff.infra.util.Logger.get("FOSS.LOG.KAFKA");

    private static final String valuesSeparator = "`@`";
    private static final String kvSeparator = "`#`";
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static List<ProducerRecord<String, String>> logs = new ArrayList<ProducerRecord<String, String>>(1024);
    private static List<ProducerRecord<String, String>> logsCache = new ArrayList<ProducerRecord<String, String>>(1024);
    private static KafkaInstant kafkaLog;
    private static long lastSend = System.currentTimeMillis();

    public static void main(String[] args) {
        init("121.40.49.223:9092", "applogs");
        start();
        log(MapHelper.toMap("name", "tyler", "date", new Date()));
        log(MapHelper.toMap("name", "tyler", "date", new Date()));
        log(MapHelper.toMap("name", "tyler", "date", new Date()));
        close();
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(2);
    }

    public static void init(String brokers, String topic, String username, String password) {
        kafkaLog = KafkaInstant.config(brokers, topic, username, password);
    }

    public static void init(String brokers, String topic) {
        kafkaLog = KafkaInstant.config(brokers, topic);
    }

    public static void start() {
        Assert.notNull(kafkaLog, "LOG.KAFKA kafka is not init, please invoke LogKafkaHelper.init to init kafka first.");
        kafkaLog.init();
        ThreadPoolHelper.executeWithFixedScheduled(new Runnable() {
            public void run() {
                send();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public static void send() {
        try {
            lock.writeLock().lock();
            List<ProducerRecord<String, String>> tmp = logs;
            logs = logsCache;
            logsCache = tmp;
            if (logsCache.size() < 1) {
                return;
            }
        } finally {
            lock.writeLock().unlock();
        }
        final ProducerRecord<String, String>[][] prss = new ProducerRecord[1][];
        try {
            lock.writeLock().lock();
            prss[0] = logsCache.toArray(new ProducerRecord[logsCache.size()]);
            logsCache.clear();
            if (prss[0].length < 1) {
                return;
            }
        } finally {
            lock.writeLock().unlock();
        }
        if (kafkaLog.getProducer() == null) {
            Logger.warn("LOG.KAFKA kafka not start yet, please invoke LogKafkaHelper.start to start Kafka first!");
            return;
        }
        try {
            ThreadPoolHelper.executeFixed(new Runnable() {
                public void run() {
                    ProducerRecord<String, String>[] prs = prss[0];
                    for (int i = 0; i < prs.length; i++) {
                        final ProducerRecord<String, String> pr = prs[i];
                        kafkaLog.getProducer().send(pr, new Callback() {
                            public void onCompletion(RecordMetadata metadata, Exception exception) {
                                if (exception != null) {
                                    try {
                                        Logger.warn("Re-add message: " + pr.key());
                                        logs.add(pr);
                                        kafkaLog.renew();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void log(String message) {
        Assert.notNull(kafkaLog, "LOG.KAFKA kafka is not init, please invoke LogKafkaHelper.init to init kafka first.");
        if (StringUtils.isNotBlank(message)) {
            saveLog(new ProducerRecord<String, String>(kafkaLog.getTopic(), IdHelper.uuid(), GsonHelper.toJsonString(MapHelper.toMap("message", message, "date", new Date()))));
        }
    }

    public static void logJson(String json) {
        Assert.notNull(kafkaLog, "LOG.KAFKA kafka is not init, please invoke LogKafkaHelper.init to init kafka first.");
        if (StringUtils.isNotBlank(json)) {
            saveLog(new ProducerRecord<String, String>(kafkaLog.getTopic(), IdHelper.uuid(), json));
        }
    }

    public static void log(final Map<?, ?> data) {
        Assert.notNull(kafkaLog, "LOG.KAFKA kafka is not init, please invoke LogKafkaHelper.init to init kafka first.");
        if (data != null && data.size() > 0) {
            saveLog(new ProducerRecord<String, String>(kafkaLog.getTopic(), IdHelper.uuid(), GsonHelper.toJsonString(data)));
        }
    }

    public static void close() {
        Assert.notNull(kafkaLog, "LOG.KAFKA kafka is not init, please invoke LogKafkaHelper.init to init kafka first.");
        send();
        ThreadPoolHelper.executeFixed(new Runnable() {
            public void run() {
                kafkaLog.getProducer().flush();
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                StreamHelper.closeWithoutError(kafkaLog.getProducer());
            }
        });
    }

    public static KeyValueConcator concator() {
        return new KeyValueConcator();
    }

    private static void saveLog(ProducerRecord pr) {
        if (kafkaLog == null) {
            Logger.warn("LOG.KAFKA kafka not init yet, please invoke LogKafkaHelper.init to init Kafka first!");
        } else if (kafkaLog.getProducer() == null) {
            Logger.warn("LOG.KAFKA kafka not start yet, please invoke LogKafkaHelper.start to start Kafka first!");
        }
        try {
            lock.readLock().lock();
            logs.add(pr);
        } finally {
            lock.readLock().unlock();
        }
        if (logs.size() < 1000) {
            return;
        }
        if (logsCache.size() > 0) {
            Logger.warn("LOG.KAFKA kafka too many unsaved logs.");
            if (logs.size() > 2000) {
                try {
                    lock.writeLock().lock();
                    Logger.warn("LOG.KAFKA UNSAVED.LOGS [NOKAFKA]: " + GsonHelper.toJsonString(logsCache));
                    logsCache.clear();
                } finally {
                    lock.writeLock().unlock();
                }
            }
        }
        send();
    }

    public static class KafkaInstant implements Closeable {

        private String id;
        private String topic;
        private Properties props;
        private Producer<String, String> producer;
        private long lastRenew = System.currentTimeMillis();

        public static KafkaInstant config(String brokers, String topic) {
            return config(brokers, topic, null, null);
        }

        public static KafkaInstant config(String brokers, String topic, String username, String password) {
            KafkaInstant ins = new KafkaInstant();
            ins.topic = PreCheckHelper.checkBlank(topic, "LOG.KAFKA kafka topic is required.");
            brokers = PreCheckHelper.checkBlank(brokers, "LOG.KAFKA kafka broker is required.");
            ins.id = brokers + ":" + topic;
            Properties props = ins.props = new Properties();

            String serializer = StringSerializer.class.getName();
            String deserializer = StringDeserializer.class.getName();

            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
            //props.put("group.id", StringUtils.defaultString(username, "FOSS.LOG") + "-consumer");
            props.put(ProducerConfig.CLIENT_ID_CONFIG, "FOSS.LOG");
            props.put(ProducerConfig.ACKS_CONFIG, "0");
            //props.put("metadata.fetch.timeout.ms", "1000");
            props.put("max.block.ms", "30000");
            //props.put("message.timeout.ms", "1000");
            //props.put("message.send.max.retries", "0");
            //props.put("enable.auto.commit", "true");
            //props.put("auto.commit.interval.ms", "1000");
            //props.put("auto.offset.reset", "earliest");
            //props.put("session.timeout.ms", "30000");
            //props.put("key.deserializer", deserializer);
            //props.put("value.deserializer", deserializer);
            props.put("key.serializer", serializer);
            props.put("value.serializer", serializer);
            if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
                String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
                String jaasCfg = String.format(jaasTemplate, username, password);
                props.put("security.protocol", "SASL_SSL");
                props.put("sasl.mechanism", "SCRAM-SHA-256");
                props.put("sasl.jaas.config", jaasCfg);
            }

            return ins;
        }

        public void init() {
            Assert.notNull(props, "LOG.KAFKA please invoke KafkaInstant.config to config first!");
            if (producer != null) {
                return;
            }
            producer = new KafkaProducer<String, String>(props);
            ShutdownHookHelper.register("FOSS.LOG.KAFKA-" + topic, producer);
        }

        public KafkaInstant renew() {
            if (System.currentTimeMillis() - lastRenew < 3 * 1000) {
                return this;
            }
            try {
                producer.close(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ShutdownHookHelper.unregister("FOSS.LOG.KAFKA-" + topic);
            producer = new KafkaProducer<String, String>(props);
            ShutdownHookHelper.register("FOSS.LOG.KAFKA-" + topic, producer);
            return this;
        }

        public void close() {
            StreamHelper.closeWithoutError(producer);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public Properties getProps() {
            return props;
        }

        public void setProps(Properties props) {
            this.props = props;
        }

        public Producer<String, String> getProducer() {
            return producer;
        }

        public void setProducer(Producer<String, String> producer) {
            this.producer = producer;
        }
    }

    /**
     * Kafka Logback 日志存储。
     */
    public static class KafkaAppender extends AppenderBase<ILoggingEvent> {
        private Encoder encoder;

        private ByteArrayOutputStream baos = null;

        public void start() {
            super.start();
            try {
                if (baos == null) {
                    baos = new ByteArrayOutputStream(2048);
                    encoder.init(baos);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 拆分 K-V 型日志。
         *
         * @param event
         */
        protected void append(ILoggingEvent event) {
            try {
                baos.reset();
                encoder.doEncode(event);
                Map<String, Object> data = new HashMap<String, Object>();
                String content = baos.toString("UTF-8");
                if (StringUtils.isBlank(content) || StringUtils.contains(content, "[NOKAFKA]")) {
                    return;
                }
                String[] kvs = StringUtils.splitByWholeSeparator(content, valuesSeparator);
                if (kvs.length == 0) {
                    return;
                } else if (kvs.length == 1) {
                    data.put("message", kvs[0]);
                } else {
                    for (String kv : kvs) {
                        String[] split = StringUtils.splitByWholeSeparator(kv, kvSeparator);
                        if (split.length != 2 || StringUtils.isBlank(split[0])) {
                            continue;
                        }
                        String key = split[0], value = split[1];
                        if (key.equals("traceid")) {
                            String[] tid = StringUtils.split(value, '/');
                            if (tid.length > 0) {
                                data.put("id", tid[0]);
                            }
                            if (tid.length > 1) {
                                data.put("ip", tid[1]);
                            }
                            if (tid.length > 1) {
                                data.put("sid", tid[2]);
                            }
                            if (tid.length > 2) {
                                data.put("uid", tid[3]);
                            }
                        } else {
                            data.put(key, value);
                        }
                    }
                }
                if (data.size() > 0) {
                    log(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Encoder getEncoder() {
            return encoder;
        }

        public void setEncoder(Encoder encoder) {
            this.encoder = encoder;
        }
    }

    /**
     * 用于拼接 K-V 型日志。
     */
    public static class KeyValueConcator {
        private StringBuilder sb = new StringBuilder(128);

        public KeyValueConcator concat(String key, String value) {
            if (StringUtils.isBlank(key)) {
                return this;
            }
            if (sb.length() > 0) {
                sb.append(valuesSeparator);
            }
            sb.append(key).append(kvSeparator).append(value);
            return this;
        }

        public String toString() {
            return sb.toString();
        }
    }
}
