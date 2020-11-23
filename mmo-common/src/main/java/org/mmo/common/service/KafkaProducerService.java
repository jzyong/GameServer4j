package org.mmo.common.service;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.mmo.common.struct.object.log.ILog;
import org.mmo.engine.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * kafka生产者客户端
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Service
public class KafkaProducerService {
    public static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerService.class);
    private KafkaProducer<String, String> producer;


    /**
     * 连接kafka
     */
    public void connect(String url, String clientId) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, url);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);
    }

    public void send(String topic, String key, String value) {
        long time = TimeUtil.currentTimeMillis();
        producer.send(new ProducerRecord<>(topic, key, value), new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                try {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("{}-{} send to partition {},offset {} in {}ms", topic, key, metadata.partition(),
                                metadata.offset(), (TimeUtil.currentTimeMillis() - time));
                    }
                } catch (Exception e) {
                    LOGGER.warn("{} {} {} send fail", topic, key, value);
                    LOGGER.error("send", e);
                }
            }
        });
    }

    /**
     * 发送日志
     *
     * @param log
     */
    public void sendLog(ILog log) {
        this.send(log.topic(), log.key(), log.value());
    }

}
