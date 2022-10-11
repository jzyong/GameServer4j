package org.jzy.game.hall.db;

import com.mongodb.client.MongoClients;
import org.jzy.game.common.config.server.HallConfig;
import org.jzy.game.common.constant.GlobalProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 游戏数据配置
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Service
public class MongoGameService {

    private MongoOperations mongoOperations;

    @Autowired
    private HallConfig hallConfig;
    @Autowired
    private GlobalProperties globalProperties;

    @PostConstruct
    public void init() {
        mongoOperations = new MongoTemplate(new SimpleMongoClientDatabaseFactory(MongoClients.create(hallConfig.getMongodbUrl()), hallConfig.getMongodbDatabase()));
    }

    public MongoOperations getMongoOperations() {
        return mongoOperations;
    }


}
