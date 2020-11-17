package org.mmo.gate.tcp.game;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.mmo.engine.io.netty.config.NettyProperties;
import org.mmo.engine.io.netty.config.NettyServerConfig;
import org.mmo.engine.io.netty.script.IChannelHandlerScript;
import org.mmo.engine.script.ScriptService;
import org.mmo.gate.service.GateExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 用户 tcp channel初始化
 *
 * @author JiangZhiYong
 */
@Component
@Scope("prototype")
public class GameTcpChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private ScriptService scriptService;
    @Autowired
    GateExecutorService gateExecutorService;
    @Autowired
    NettyProperties nettyProperties;
    @Autowired
    GameTcpService tcpService;
    
    public GameTcpChannelInitializer() {

    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("Codec", new GameTcpByteToMessageCodec(scriptService));
        ch.pipeline().addLast("MessageHandler", new GameTcpServerHandler(scriptService, gateExecutorService,tcpService));

        NettyServerConfig nettyServerConfig = nettyProperties.getServerConfigs().get(1);
        int bothIdleTime = Math.min(nettyServerConfig.getReaderIdleTime(), nettyServerConfig.getWriterIdleTime());
        ch.pipeline().addLast("IdleStateHandler", new IdleStateHandler(nettyServerConfig.getReaderIdleTime(),
                nettyServerConfig.getWriterIdleTime(), bothIdleTime));
        scriptService.consumerScript("GameChannelHandlerScript",(IChannelHandlerScript script)->script.initChannel(ch));
    }
}
