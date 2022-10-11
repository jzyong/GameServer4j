package org.jzy.game.gate.tcp.user;


import com.jzy.javalib.network.netty.config.NettyServerConfig;
import com.jzy.javalib.network.netty.tcp.TcpServer;
import com.jzy.javalib.network.netty.tcp.TcpService;
import org.jzy.game.common.config.server.GateConfig;
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

    private TcpServer nettyServer;

    @Autowired
    private UserTcpChannelInitializer userTcpChannelInitializer;
    @Autowired
    private GateConfig gateConfig;

    public UserTcpService() {
    }


    @PostConstruct
    public void start() {
        LOG.debug(" run user tcp ... ");
        nettyServer=new TcpServer();
        NettyServerConfig nettyServerConfig = new NettyServerConfig();
        nettyServerConfig.setPort(gateConfig.getClientTcpPort());
        nettyServer.setNettyServerConfig(nettyServerConfig);
        nettyServer.setChannelInitializer(userTcpChannelInitializer);
        nettyServer.start();
    }

    @PreDestroy
    public void stop() {
        LOG.debug(" stop user tcp ... ");
        nettyServer.stop();
    }

}
