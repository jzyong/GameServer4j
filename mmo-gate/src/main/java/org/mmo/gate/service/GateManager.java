package org.mmo.gate.service;


import org.mmo.engine.io.netty.config.NettyProperties;
import org.mmo.engine.server.ServerProperties;
import org.mmo.gate.tcp.game.GameTcpService;
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
    NettyProperties nettyProperties;
    
    @Autowired
    ServerProperties serverProperties;


    @Autowired
    UserService userService;

    @Autowired
    GateToLoginRpcService gateToLoginRpcService;

    @Autowired
    GateService gateService;

    @Autowired
    GameTcpService gameTcpService;


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
    

	public ServerProperties getServerProperties() {
		return serverProperties;
	}


    public UserService getUserService() {
        return userService;
    }

    public GateToLoginRpcService getGateToLoginRpcService() {
        return gateToLoginRpcService;
    }

    public GateService getGateServerService() {
        return gateService;
    }

    public GameTcpService getGameTcpService() {
        return gameTcpService;
    }
}
