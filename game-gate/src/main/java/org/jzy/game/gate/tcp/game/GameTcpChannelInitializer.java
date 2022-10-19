package org.jzy.game.gate.tcp.game;

import com.jzy.javalib.base.script.ScriptManager;
import com.jzy.javalib.network.netty.IChannelHandlerScript;
import com.jzy.javalib.network.netty.config.NettyServerConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.jzy.game.common.config.server.GateConfig;
import org.jzy.game.gate.service.GateExecutorService;
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
    GateExecutorService gateExecutorService;
    @Autowired
    private GateConfig gateConfig;

    public GameTcpChannelInitializer() {

    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast("Codec", new GameTcpByteToMessageCodec());
        ch.pipeline().addLast("MessageHandler", new GameTcpServerHandler(gateExecutorService));

        NettyServerConfig nettyServerConfig = new NettyServerConfig();
        nettyServerConfig.setPort(gateConfig.getGameTcpPort());
        int bothIdleTime = Math.min(nettyServerConfig.getReaderIdleTime(), nettyServerConfig.getWriterIdleTime());
        ch.pipeline().addLast("IdleStateHandler", new IdleStateHandler(nettyServerConfig.getReaderIdleTime(),
                nettyServerConfig.getWriterIdleTime(), bothIdleTime));
        ScriptManager.getInstance().consumerScript("GameChannelHandlerScript", (IChannelHandlerScript script) -> script.initChannel(ch));
    }
}
