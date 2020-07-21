package org.mmo.gate.server.tcp.server.user;

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
public class UserTcpChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private ScriptService scriptService;
    @Autowired
    GateExecutorService gateExecutorService;
    @Autowired
    NettyProperties nettyProperties;
    @Autowired
    UserTcpService tcpService;
    
    public UserTcpChannelInitializer() {

    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("Codec", new UserTcpByteToMessageCodec(scriptService));
        ch.pipeline().addLast("MessageHandler", new UserTcpServerHandler(scriptService, gateExecutorService,tcpService));

        NettyServerConfig nettyServerConfig = nettyProperties.getServerConfigs().get(0);
        int bothIdleTime = Math.min(nettyServerConfig.getReaderIdleTime(), nettyServerConfig.getWriterIdleTime());
        ch.pipeline().addLast("IdleStateHandler", new IdleStateHandler(nettyServerConfig.getReaderIdleTime(),
                nettyServerConfig.getWriterIdleTime(), bothIdleTime));
        scriptService.consumerScript("UserChannelHandlerScript",(IChannelHandlerScript script)->script.initChannel(ch));
    }
}
