package org.mmo.gate.script.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.Attribute;
import io.netty.util.concurrent.ScheduledFuture;
import org.mmo.engine.io.message.MsgUtil;
import org.mmo.engine.io.netty.script.IChannelHandlerScript;
import org.mmo.engine.util.TimeUtil;
import org.mmo.gate.server.tcp.server.user.UserTcpServerHandler;
import org.mmo.gate.service.GateManager;
import org.mmo.gate.struct.RC4;
import org.mmo.gate.struct.User;
import org.mmo.message.MIDMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;


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
