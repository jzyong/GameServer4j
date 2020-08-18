package org.mmo.login.service;


import io.grpc.stub.StreamObserver;
import org.mmo.common.constant.ServerType;
import org.mmo.common.constant.ThreadType;
import org.mmo.common.scripts.IServerScript;
import org.mmo.engine.script.ScriptService;
import org.mmo.engine.server.ServerInfo;
import org.mmo.engine.server.ServerProperties;
import org.mmo.engine.thread.Scene.AbstractScene;
import org.mmo.message.ServerRegisterUpdateResponse;
import org.mmo.message.ServerServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


/**
 * 服务器管理
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Service
public class LoginServerService extends AbstractScene {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServerService.class);


    @Autowired
    private ScriptService scriptService;
    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private LoginExecutorService loginExecutorService;


    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        LOGGER.info("服务器：{}-{} 启动...", serverProperties.getId(), serverProperties.getName());

        scriptService.init((str) -> {
            LOGGER.error("脚本加载错误:{}", str);
            System.exit(0);
        });
        loginExecutorService.registerScene(ThreadType.server.toString(), this);
        scheduleAtFixedRate(() -> {
            scriptService.consumerScript("LoginServerScript", (IServerScript script) -> script.updateServerInfo());
        }, 3, ServerProperties.ServerRegisterHeart, TimeUnit.SECONDS);

    }


    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        LOGGER.info("服务器：{}-{} 关闭...", serverProperties.getId(), serverProperties.getName());

    }


}
