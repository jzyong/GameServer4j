package org.mmo.gate.server.tcp.server.game;


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
 * 游戏 tcp 通信
 */
@Service
public class GameTcpService extends TcpService {
    private static final Logger LOG = LoggerFactory.getLogger(GameTcpService.class);

    @Autowired
    private TcpServer nettyServer;

    @Autowired
    private GameTcpChannelInitializer tcpChannelInitializer;
    @Autowired
    private NettyProperties nettyProperties;

    public GameTcpService() {
    }


    @PostConstruct
    public void start() {
        LOG.debug(" run game tcp ... ");
        NettyServerConfig nettyServerConfig = nettyProperties.getServerConfigs().get(1);
        nettyServer.setNettyServerConfig(nettyServerConfig);
        nettyServer.setChannelInitializer(tcpChannelInitializer);
        nettyServer.start();
    }

    @PreDestroy
    public void stop() {
        LOG.debug(" stop game tcp ... ");
        nettyServer.stop();
    }

}
