package org.mmo.login.service;


import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.mmo.common.config.server.LoginConfig;
import org.mmo.common.config.server.MongoConfig;
import org.mmo.common.config.server.ServiceConfig;
import org.mmo.common.constant.GlobalProperties;
import org.mmo.common.constant.ServiceName;
import org.mmo.common.constant.ThreadType;
import org.mmo.common.constant.ZKNode;
import org.mmo.common.service.KafkaProducerService;
import org.mmo.common.service.ZkClientService;
import org.mmo.engine.script.ScriptService;
import org.mmo.engine.thread.Scene.AbstractScene;
import org.mmo.engine.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * 服务器管理
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Service
public class LoginService extends AbstractScene {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    private ServiceInstance<ServiceConfig> serviceInstance;

    @Autowired
    private ScriptService scriptService;
    @Autowired
    private LoginConfig loginConfig;
    @Autowired
    private LoginExecutorService loginExecutorService;
    @Value("${config.mongodb.url}")
    private String mongoConfigUrl;
    @Value("${config.mongodb.database}")
    private String mongoConfigDatabase;
    @Autowired
    private ZkClientService zkClientService;
    @Autowired
    private GlobalProperties globalProperties;
    @Value("${spring.data.mongodb.host}")
    private String mongoHost;
    @Value("${spring.data.mongodb.port}")
    private int mongoPort;
    @Value("${spring.data.mongodb.authentication-database}")
    private String mongoAuthentication;
    @Value("${spring.data.mongodb.database}")
    private String mongoDatabase;
    @Value("${spring.data.mongodb.password}")
    private String mongoPassword;
    @Value("${spring.data.mongodb.username}")
    private String mongoUsername;
    @Autowired
    private KafkaProducerService kafkaProducerService;


    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        try {
            LOGGER.info("login server start：{}-->{} ", loginConfig.getId(), loginConfig.toString());
            //推送配置，数据库配置只在此推送
            zkClientService.pushConfig(ZKNode.LoginConfig.getKey(globalProperties.getProfile(), loginConfig.getId()), loginConfig);
            serviceInstance = ServiceInstance.<ServiceConfig>builder()
                    .id(String.valueOf(loginConfig.getId()))
                    .registrationTimeUTC(TimeUtil.currentTimeMillis())
                    .name(ServiceName.LoginRpc.name())
                    .address(loginConfig.getPrivateIp())
                    .payload(new ServiceConfig())
                    .port(loginConfig.getRpcPort())
                    .uriSpec(new UriSpec("{address}:{port}"))
                    .build();
            zkClientService.starService(ZKNode.ServicePath.getKey(globalProperties.getProfile()), serviceInstance);
            zkClientService.pushConfig(ZKNode.MongoExcelConfig.getKey(globalProperties.getProfile()), new MongoConfig(mongoConfigUrl, mongoConfigDatabase));
            zkClientService.pushConfig(ZKNode.MongoGameConfig.getKey(globalProperties.getProfile()), new MongoConfig(mongoHost, mongoPort, mongoAuthentication, mongoDatabase, mongoPassword, mongoUsername));

            kafkaProducerService.connectLog("mmo.login"+loginConfig.getId());

            scriptService.init((str) -> {
                LOGGER.error("load scripts error:{}", str);
                System.exit(0);
            });
            loginExecutorService.registerScene(ThreadType.server.toString(), this);
        } catch (Exception e) {
            LOGGER.error("login server start", e);
        }


    }


    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        zkClientService.unregisterService(serviceInstance);
        LOGGER.info("login server stop：{}-->{} ", loginConfig.getId(), loginConfig.toString());

    }


}
