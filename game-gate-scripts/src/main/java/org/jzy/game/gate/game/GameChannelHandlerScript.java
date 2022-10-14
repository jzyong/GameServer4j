package org.jzy.game.gate.game;

import com.jzy.javalib.network.netty.IChannelHandlerScript;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 游戏客户端用户netty连接
 *
 * @author JiangZhiYong
 */
public class GameChannelHandlerScript implements IChannelHandlerScript {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameChannelHandlerScript.class);

    @Override
    public void init() {
    }

    @Override
    public boolean isBlackList(ChannelHandlerContext ctx) {
        return false;
    }

    @Override
    public void initChannel(SocketChannel ch, Object... object) {


    }


    @Override
    public void channelActive(ChannelHandlerContext ctx, Object... objects) {

    }

    @Override
    public boolean inBoundMessageCheck(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        return true;
    }


    @Override
    public void destroy() {
    }

}
