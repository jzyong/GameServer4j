package org.jzy.game.api.db;

import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * mongodb服务管理
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Service
public class MongoDbService {


    @Value("${api.mongodb.url}")
    private String url;
    @Value("${api.mongodb.database}")
    private String database;
    private MongoOperations mongoOperations;

    @PostConstruct
    public void init() {
        mongoOperations = new MongoTemplate(new SimpleMongoClientDatabaseFactory(MongoClients.create(url), database));
    }

    public MongoOperations getMongoOperations() {
        return mongoOperations;
    }

}
