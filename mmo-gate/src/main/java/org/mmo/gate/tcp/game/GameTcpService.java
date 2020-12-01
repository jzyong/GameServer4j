package org.mmo.gate.tcp.game;


import org.mmo.common.config.server.GateConfig;
import org.mmo.engine.io.netty.config.NettyServerConfig;
import org.mmo.engine.io.netty.tcp.TcpServer;
import org.mmo.engine.io.service.TcpService;
import org.mmo.common.struct.server.GameServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private GateConfig gateConfig;

    /**
     * 游戏服务器 TODO 游戏服关闭，移除操作
     */
    private final Map<Integer, GameServerInfo> gameServers=new ConcurrentHashMap<>();

    public GameTcpService() {
    }


    @PostConstruct
    public void start() {
        LOG.debug(" run game tcp ... ");
        NettyServerConfig nettyServerConfig = new NettyServerConfig();
        nettyServerConfig.setPort(gateConfig.getGameTcpPort());
        nettyServer.setNettyServerConfig(nettyServerConfig);
        nettyServer.setChannelInitializer(tcpChannelInitializer);
        nettyServer.start();
    }

    @PreDestroy
    public void stop() {
        LOG.debug(" stop game tcp ... ");
        nettyServer.stop();
    }

    public Map<Integer, GameServerInfo> getGameServers() {
        return gameServers;
    }
}
