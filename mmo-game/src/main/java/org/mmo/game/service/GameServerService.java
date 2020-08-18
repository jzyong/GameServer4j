package org.mmo.game.service;

import org.mmo.common.constant.ThreadType;
import org.mmo.common.scripts.IServerScript;
import org.mmo.engine.script.ScriptService;
import org.mmo.engine.server.ServerProperties;
import org.mmo.engine.thread.Scene.AbstractScene;
import org.mmo.engine.util.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 服务器管理入口
 * @author jzy
 */
@Service
public class GameServerService extends AbstractScene {
    public static final Logger LOGGER= LoggerFactory.getLogger(GameServerService.class);

    @Autowired
    private ScriptService scriptService;
    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private ExecutorService executorService;

    @PostConstruct
    public void init(){
        IdUtil.SERVER_ID=serverProperties.getId();
        LOGGER.info("服务器：{}-{} 启动...", serverProperties.getId(), serverProperties.getName());
        scriptService.init((str) -> {
            LOGGER.error("脚本加载错误:{}", str);
            System.exit(0);
        });

        executorService.registerScene(ThreadType.server.toString(), this);
        scheduleAtFixedRate(() -> {
            scriptService.consumerScript("GameServerScript", (IServerScript script) -> script.updateServerInfo());
        }, 3, ServerProperties.ServerRegisterHeart, TimeUnit.SECONDS);
    }
}
