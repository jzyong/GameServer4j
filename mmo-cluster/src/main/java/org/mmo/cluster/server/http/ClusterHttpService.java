package org.mmo.cluster.server.http;

import org.mmo.engine.io.netty.config.NettyProperties;
import org.mmo.engine.io.netty.http.HttpServer;
import org.mmo.engine.io.service.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * http服务器
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Service
public class ClusterHttpService extends HttpService {

    @Autowired
    private HttpServer httpServer;
    @Autowired
    private ClusterHttpChannelInitializer clusterHttpChannelInititialier;
    @Autowired
    private NettyProperties nettyProperties;

    @PostConstruct
    public void init(){
        httpServer.setNettyServerConfig(nettyProperties.getServerConfigs().get(1));
        httpServer.setChannelInitializer(clusterHttpChannelInititialier);
        httpServer.start();
    }

    @PreDestroy
    public void destroy(){
        httpServer.stop();
    }

}
