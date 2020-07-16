package org.mmo.gate.server.tcp.client.cluster;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.mmo.engine.io.netty.config.NettyClientConfig;
import org.mmo.engine.io.netty.config.NettyProperties;
import org.mmo.engine.script.ScriptService;
import org.mmo.gate.service.GateExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 登陆服tcp channel初始化
 * 
 * @author JiangZhiYong
 * @date 2018/12/11
 */
@Deprecated
@Component
@Scope("prototype")
public class GateToClusterChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private ScriptService scriptService;
    @Autowired
    GateExecutorService executorService;
    @Autowired
    NettyProperties nettyProperties;
    @Autowired
    GateToClusterService tcpService;

    public GateToClusterChannelInitializer() {

    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("Codec", new GateToClusterMessageCodec());
        ch.pipeline().addLast("MessageHandler", new GateToClusterClientHandler(scriptService,executorService,tcpService));

        NettyClientConfig nettyClientConfig = nettyProperties.getClientConfigs().get(0);
        int bothIdleTime = Math.min(nettyClientConfig.getReaderIdleTime(), nettyClientConfig.getWriterIdleTime());
        ch.pipeline().addLast("IdleStateHandler", new IdleStateHandler(nettyClientConfig.getReaderIdleTime(),
                nettyClientConfig.getWriterIdleTime(), bothIdleTime));
    }
}