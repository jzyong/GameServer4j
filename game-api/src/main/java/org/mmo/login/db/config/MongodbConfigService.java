package org.mmo.login.db.config;

import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * mongodb数据库配置数据
 * <br>
 *     配置数据和玩家数据分开存储
 * @author jzy
 */
@Service
public class MongodbConfigService {

    private MongoOperations  mongoOperations;

    @Value("${config.mongodb.url}")
    private String url;
    @Value("${config.mongodb.database}")
    private String database;

    @PostConstruct
    public void init(){
        mongoOperations = new MongoTemplate(new SimpleMongoClientDbFactory(MongoClients.create(url), database));
    }

    public MongoOperations getMongoOperations() {
        return mongoOperations;
    }



}
