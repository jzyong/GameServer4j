package org.jzy.game.hall.service;

import org.junit.Ignore;
import org.junit.Test;
import org.jzy.game.common.constant.LogTopic;
import org.jzy.game.common.service.KafkaProducerService;

/**
 * @author jzy
 * @mail 359135103@qq.com
 */
//@Ignore
public class KafkaProducerServiceTest {

    @Test
    public void testSendMessage() throws InterruptedException {
        KafkaProducerService kafkaProducerService = new KafkaProducerService();
        kafkaProducerService.connect("127.0.0.1:9092", "mmo.game");
        for (int i = 0; i < 100; i++) {
            kafkaProducerService.send(LogTopic.Login.getName(), String.valueOf(i), "hello" + i);
            Thread.sleep(1000);
        }

    }
}
