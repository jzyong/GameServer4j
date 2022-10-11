package org.jzy.game.manage.service;

import org.jzy.game.common.config.server.ManageConfig;
import org.jzy.game.common.constant.LogTopic;
import org.jzy.game.manage.util.kafka.LogConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 日志消耗
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Service
public class LogConsumerService {

    private LogConsumer logConsumer;

    @Autowired
    private ManageConfig manageConfig;


    @PostConstruct
    public void init() {
        //同时开两个manage两边同时拉取一样的数据？
        if(manageConfig.isFetchKafkaLog()){
            logConsumer = new LogConsumer(LogTopic.toNameList(), "manage" + manageConfig.getId(), manageConfig.getKafkaUrl());
            logConsumer.start();
        }
    }


    @PreDestroy
    public void destroy() {
        if (logConsumer != null) {
            logConsumer.shutdown();
        }
    }

}
