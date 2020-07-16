package org.mmo.gate.server.tcp.client.cluster;


import org.mmo.engine.io.netty.config.NettyClientConfig;
import org.mmo.engine.io.netty.config.NettyProperties;
import org.mmo.engine.io.netty.tcp.TcpClient;
import org.mmo.engine.io.service.TcpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 网关连接cluster，使用rpc替换
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Deprecated
@Service
public class GateToClusterService extends TcpService {
    private static final Logger LOGGER= LoggerFactory.getLogger(GateToClusterService.class);

    @Autowired
    private TcpClient tcpClient;

    @Autowired
    private GateToClusterChannelInitializer channelInitializer;

    @Autowired
    private NettyProperties nettyProperties;


    //@PostConstruct
    public void start(){
        LOGGER.debug("连接cluster...");
        NettyClientConfig nettyClientConfig = nettyProperties.getClientConfigs().get(0);
        tcpClient.setNettyClientConfig(nettyClientConfig);
        tcpClient.setChannelInitializer(channelInitializer);
        tcpClient.start();
    }


    @PreDestroy
    public void stop(){
        LOGGER.debug("关闭cluster 连接....");
        tcpClient.stop();
    }
}
