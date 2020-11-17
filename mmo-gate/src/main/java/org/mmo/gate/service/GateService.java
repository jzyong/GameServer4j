package org.mmo.gate.service;

import org.mmo.common.config.server.GateConfig;
import org.mmo.common.constant.ThreadType;
import org.mmo.common.constant.ZKNode;
import org.mmo.common.scripts.IServerScript;
import org.mmo.common.service.ZkClientService;
import org.mmo.engine.script.ScriptService;
import org.mmo.engine.server.ServerProperties;
import org.mmo.engine.thread.Scene.AbstractScene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * 网关服务器管理
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Service
public class GateService extends AbstractScene {
    private static final Logger LOGGER= LoggerFactory.getLogger(GateService.class);

    @Autowired
    private GateExecutorService executorService;

    @Autowired
    private ScriptService scriptService;
    @Autowired
    private GateConfig gateConfig;
    @Autowired
    private ZkClientService zkClientService;

    @Value("${global.profile}")
    private String profile;



    @PostConstruct
    public void init(){
        LOGGER.debug("网关服务器启动：{}-{}...",gateConfig.toString());
        scriptService.init((str) -> {
            LOGGER.error("脚本加载错误：{}",str);
            System.exit(0);

        });

        //推送配置
        zkClientService.pushConfig(ZKNode.GateConfig.getKey(profile,gateConfig.getId()), gateConfig);
        //TODO 监听zookeeper 游戏服变化情况

        executorService.registerScene(ThreadType.server.toString(),this);
//        //执行服务器状态更新
//        this.scheduleWithFixedDelay(()->{
//            scriptService.consumerScript("GateServerScript",(IServerScript script)-> script.updateServerInfo());
//        },3,3, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void destroy(){
        LOGGER.info("销毁");
    }

}
