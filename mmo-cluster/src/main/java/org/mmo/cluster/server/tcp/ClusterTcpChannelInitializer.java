package org.mmo.cluster.server.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.mmo.cluster.service.ClusterExecutorService;
import org.mmo.engine.io.netty.config.NettyProperties;
import org.mmo.engine.io.netty.config.NettyServerConfig;
import org.mmo.engine.script.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 登陆服tcp channel初始化
 *
 * @author JiangZhiYong
 */
@Component
@Scope("prototype")
public class ClusterTcpChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private ScriptService scriptService;
    @Autowired
    ClusterExecutorService clusterExecutorService;
    @Autowired
    NettyProperties nettyProperties;
    @Autowired
    ClusterTcpService tcpService;
    
    public ClusterTcpChannelInitializer() {

    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("Codec", new ClusterTcpByteToMessageCodec());
        ch.pipeline().addLast("MessageHandler", new ClusterTcpServerHandler(scriptService,clusterExecutorService,tcpService));

        NettyServerConfig nettyServerConfig = nettyProperties.getServerConfigs().get(0);
        int bothIdleTime = Math.min(nettyServerConfig.getReaderIdleTime(), nettyServerConfig.getWriterIdleTime());
        ch.pipeline().addLast("IdleStateHandler", new IdleStateHandler(nettyServerConfig.getReaderIdleTime(),
                nettyServerConfig.getWriterIdleTime(), bothIdleTime));
    }
}
