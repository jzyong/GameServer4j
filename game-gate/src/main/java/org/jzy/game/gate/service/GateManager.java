package org.jzy.game.gate.service;


import org.jzy.game.common.config.server.GateConfig;
import org.jzy.game.gate.tcp.game.GameTcpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 各种service注入
 * <br>
 *     脚本暂时无法调用Service
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Service
public class GateManager {
    private static GateManager instance;


    @Autowired
    GateExecutorService executorService;

    @Autowired
    UserService userService;

    @Autowired
    GateToLoginRpcService gateToLoginRpcService;

    @Autowired
    GateService gateService;

    @Autowired
    GameTcpService gameTcpService;
    @Autowired
    private GateConfig gateConfig;



    @PostConstruct
    public void init(){
        instance=this;
    }

    public static GateManager getInstance(){
        return   instance;
    }

    public GateExecutorService getExecutorService() {
        return executorService;
    }
    
    public UserService getUserService() {
        return userService;
    }

    public GateToLoginRpcService getGateToLoginRpcService() {
        return gateToLoginRpcService;
    }

    public GateService getGateService() {
        return gateService;
    }

    public GameTcpService getGameTcpService() {
        return gameTcpService;
    }

    public GateConfig getGateConfig() {
        return gateConfig;
    }
}
