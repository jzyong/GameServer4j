package org.mmo.gate.service;

import org.mmo.common.constant.ThreadType;
import org.mmo.common.scripts.IServerScript;
import org.mmo.engine.script.ScriptService;
import org.mmo.engine.server.ServerProperties;
import org.mmo.engine.thread.Scene.AbstractScene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GateServerService extends AbstractScene {
    private static final Logger LOGGER= LoggerFactory.getLogger(GateServerService.class);

    @Autowired
    private GateExecutorService executorService;

    @Autowired
    private ScriptService scriptService;
    @Autowired
    private ServerProperties serverProperties;

    @PostConstruct
    public void init(){
        LOGGER.debug("服务器启动：{}-{}...",serverProperties.getId(),serverProperties.getName());
        scriptService.init((str) -> {
            LOGGER.error("脚本加载错误：{}",str);
            System.exit(0);

        });
        executorService.registerScene(ThreadType.server.toString(),this);
        //执行服务器状态更新
        this.scheduleWithFixedDelay(()->{
            scriptService.consumerScript("GateServerScript",(IServerScript script)-> script.updateServerInfo());
        },3,5, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void destroy(){
        LOGGER.info("销毁");
    }

}
