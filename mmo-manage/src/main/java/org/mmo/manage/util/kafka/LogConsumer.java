package org.mmo.manage.util.kafka;

import com.alibaba.fastjson.JSON;
import kafka.utils.ShutdownableThread;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.mmo.common.constant.LogTopic;
import org.mmo.common.struct.object.log.LoginLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collection;
import java.util.Properties;

/**
 * 日志消费者
 */
public class LogConsumer extends ShutdownableThread {
    public static final Logger LOGGER = LoggerFactory.getLogger(LogConsumer.class);
    private final KafkaConsumer<String, String> consumer;
    private final String groupId;
    private Collection<String> topics;

    /**
     *
     * @param topics
     * @param groupId A unique string that identifies the consumer group this consumer belongs to. This property is required if the consumer uses either the group management functionality by using subscribe(topic) or the Kafka-based offset management strategy.
     * @param url ip:port,ip:port
     */
    public LogConsumer(Collection<String> topics,
                       final String groupId,
                       final String url) {
        super("KafkaConsumer" + groupId, false);
        this.groupId = groupId;
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, url);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        //What to do when there is no initial offset in Kafka or if the current offset does not exist any more on the server (e.g. because that data has been deleted):
        //earliest: automatically reset the offset to the earliest offset
        //latest: automatically reset the offset to the latest offset
        //none: throw exception to the consumer if no previous offset is found for the consumer's group
        //anything else: throw exception to the consumer.
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        consumer = new KafkaConsumer<>(props);
        this.topics = topics;
        consumer.subscribe(this.topics);
    }

    KafkaConsumer get() {
        return consumer;
    }

    @Override
    public void doWork() {

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
        for (ConsumerRecord<String, String> record : records) {
            LOGGER.debug(groupId + " received message : from partition " + record.partition() + ", (" + record.topic() + "," + record.key() + ", " + record.value() + ") at offset " + record.offset());
            LogTopic logTopic = LogTopic.getLogTopic(record.topic());
            switch (logTopic) {
                case Login -> {
                    LoginLog loginLog = JSON.parseObject(record.value(), LoginLog.class);
                    //TODO 存数据库
                    //  LOGGER.info("Login Log:{}",record.value());
                }
            }
        }
        consumer.commitAsync();
    }


}
