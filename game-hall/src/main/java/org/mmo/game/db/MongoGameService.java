package org.mmo.game.db;

import com.mongodb.client.MongoClients;
import org.mmo.common.config.server.MongoConfig;
import org.mmo.common.constant.GlobalProperties;
import org.mmo.common.constant.ZKNode;
import org.mmo.common.service.ZkClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
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
    private ZkClientService zkClientService;
    @Autowired
    private GlobalProperties globalProperties;

    @PostConstruct
    public void init() {
        MongoConfig mongoConfig = zkClientService.getConfig(ZKNode.MongoGameConfig.getKey(globalProperties.getProfile()), MongoConfig.class);
        mongoOperations = new MongoTemplate(new SimpleMongoClientDbFactory(MongoClients.create(mongoConfig.buildUrl()), mongoConfig.getDatabase()));
    }

    public MongoOperations getMongoOperations() {
        return mongoOperations;
    }


}
