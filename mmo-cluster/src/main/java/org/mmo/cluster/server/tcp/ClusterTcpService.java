package org.mmo.cluster.server.tcp;


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

@Service
public class ClusterTcpService extends TcpService {
    private static final Logger LOG = LoggerFactory.getLogger(ClusterTcpService.class);

    @Autowired
    private TcpServer nettyServer;

    @Autowired
    private ClusterTcpChannelInitializer clusterTcpChannelInitializer;
    @Autowired
    private NettyProperties nettyProperties;

    public ClusterTcpService() {
    }


    @PostConstruct
    public void start() {
        LOG.debug(" run cluster tcp ... ");
        NettyServerConfig nettyServerConfig = nettyProperties.getServerConfigs().get(0);
        nettyServer.setNettyServerConfig(nettyServerConfig);
        nettyServer.setChannelInitializer(clusterTcpChannelInitializer);
        nettyServer.start();
    }

    @PreDestroy
    public void stop() {
        LOG.debug(" stop ... ");
        nettyServer.stop();
    }

}
