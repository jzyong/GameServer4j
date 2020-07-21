package org.mmo.gate.service;


import org.mmo.engine.io.netty.config.NettyProperties;
import org.mmo.engine.server.ServerProperties;
import org.mmo.gate.server.tcp.client.cluster.GateToClusterService;
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
    GateToClusterRpcService gateToClusterRpcService;

    @Autowired
    UserService userService;


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
    
    public NettyProperties getNettyProperties() {
    	return nettyProperties;
    }

	public ServerProperties getServerProperties() {
		return serverProperties;
	}


    public GateToClusterRpcService getGateToClusterRpcService() {
        return gateToClusterRpcService;
    }

    public UserService getUserService() {
        return userService;
    }
}
