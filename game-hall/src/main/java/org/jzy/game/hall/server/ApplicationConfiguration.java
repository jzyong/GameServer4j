package org.jzy.game.hall.server;

import org.jzy.game.hall.db.MongoGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * 配置
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Configuration
public class ApplicationConfiguration {

    @Autowired
    private MongoGameService mongoGameService;

    /**
     * 必须注册？
     * @return
     */
    @Bean
    public MongoTemplate mongoTemplate() {
        return (MongoTemplate) mongoGameService.getMongoOperations();
    }

}
