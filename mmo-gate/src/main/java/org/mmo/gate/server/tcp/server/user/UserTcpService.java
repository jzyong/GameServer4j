package org.mmo.gate.server.tcp.server.user;


import org.mmo.engine.io.netty.config.NettyProperties;
import org.mmo.engine.io.netty.config.NettyServerConfig;
import org.mmo.engine.io.netty.tcp.TcpServer;
import org.mmo.engine.io.service.TcpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 用户 tcp 通信
 */
@Service
public class UserTcpService extends TcpService {
    private static final Logger LOG = LoggerFactory.getLogger(UserTcpService.class);

    @Autowired
    private TcpServer nettyServer;

    @Autowired
    private UserTcpChannelInitializer userTcpChannelInitializer;
    @Autowired
    private NettyProperties nettyProperties;

    public UserTcpService() {
    }


    @PostConstruct
    public void start() {
        LOG.debug(" run user tcp ... ");
        NettyServerConfig nettyServerConfig = nettyProperties.getServerConfigs().get(0);
        nettyServer.setNettyServerConfig(nettyServerConfig);
        nettyServer.setChannelInitializer(userTcpChannelInitializer);
        nettyServer.start();
    }

    @PreDestroy
    public void stop() {
        LOG.debug(" stop ... ");
        nettyServer.stop();
    }

}
