package org.jzy.game.gate.user;

import com.jzy.javalib.base.util.ByteUtil;
import com.jzy.javalib.base.util.TimeUtil;
import com.jzy.javalib.network.io.message.MsgUtil;
import com.jzy.javalib.network.netty.IChannelHandlerScript;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.Attribute;
import org.jzy.game.gate.service.GateManager;
import org.jzy.game.gate.struct.RC4;
import org.jzy.game.gate.struct.User;
import org.jzy.game.gate.tcp.user.UserTcpServerHandler;
import org.jzy.game.proto.MID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 游戏客户端用户netty连接
 *
 * @author JiangZhiYong
 */
public class UserChannelHandlerScript implements IChannelHandlerScript {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserChannelHandlerScript.class);

    /**
     * 使用消息加密
     */
    public static final boolean MESSAGE_ENCRYPTION = true;

    /**
     * 消息检测间隔
     */
    private static final int MessageCheckInterval = 5 * 60 * 1000;
    /**
     * 消息检测个数
     */
    private static final int MessageCheckCount = 600;

    /**
     * ip黑名单
     */
    private List<String> ipBlackList = new ArrayList<>();

    @Override
    public void init() {
        // 初始化ip黑名单
//        ipBlackList.add("192.168.3.35");
    }

    @Override
    public boolean isBlackList(ChannelHandlerContext ctx) {
        String ip = MsgUtil.getIp(ctx.channel());
        if (ipBlackList.contains(ip)) {
            return true;
        }
        return false;
    }

    @Override
    public void initChannel(SocketChannel ch, Object... object) {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx, Object... objects) {
        Channel channel = ctx.channel();
        LOGGER.info("{} 已打开连接", MsgUtil.getRemoteIpPort(channel));
        // 属性设置
        User user = new User(channel);
        GateManager.getInstance().getUserService().onSocketConnect(user);
        Attribute<User> userAttr = channel.attr(UserTcpServerHandler.USER);
        userAttr.set(user);
        Attribute<Integer> requestCountAttr = channel.attr(UserTcpServerHandler.REQUEST_COUNT);
        requestCountAttr.set(0);
        Attribute<Long> requestResetTimeAttr = channel.attr(UserTcpServerHandler.REQUEST_RESET_TIME);
        requestResetTimeAttr.set(TimeUtil.currentTimeMillis());
        // 统计请求消息id
        if (LOGGER.isDebugEnabled()) {
            Attribute<Map<Integer, Integer>> requestMessageIdAttr = channel
                    .attr(UserTcpServerHandler.REQUEST_MESSAGE_IDS);
            requestMessageIdAttr.set(new HashMap<>());
        }

        // 消息打包发送
        if (GateManager.getInstance().getGateConfig().isMessageMerge()) {
            String name = Thread.currentThread().getName();
            GateManager.getInstance().getUserTcpService().addMergeMessageUser(user, name);
        }

        // 加密
        if (MESSAGE_ENCRYPTION) {
            byte[] randomKey = RC4.getRandomKey();
            RC4 rc4 = new RC4(randomKey);
            user.setRc4(rc4);

            int length = MsgUtil.ClientHeaderExcludeLength + randomKey.length;
            ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(length + 4);
            byteBuf.writeIntLE(length);
            byteBuf.writeIntLE(-1);
            byteBuf.writeIntLE(0);
            byteBuf.writeIntLE(0);
            byteBuf.writeBytes(randomKey);
            ctx.writeAndFlush(byteBuf);
            LOGGER.trace("{} 密钥：{}", MsgUtil.getRemoteIpPort(channel), ByteUtil.bytesToHex(randomKey));
        }

        // 添加返回消息统计
        if (LOGGER.isTraceEnabled()) {
            Attribute<Integer> countAttr = ctx.channel().attr(UserTcpServerHandler.RESPONSE_COUNT);
            countAttr.set(0);
            Attribute<Long> timeAttr = ctx.channel().attr(UserTcpServerHandler.RESPONSE_RESET_TIME);
            timeAttr.set(TimeUtil.currentTimeMillis());
            Attribute<Map<Integer, Integer>> idAttrs = ctx.channel().attr(UserTcpServerHandler.RESPONSE_MESSAGE_IDS);
            idAttrs.set(new HashMap<>());
        }
    }

    @Override
    public boolean inBoundMessageCheck(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        // 其他验证，加解密
        return checkRequestRatio(ctx, byteBuf);
    }

    /**
     * 检测消息频率
     *
     * @param ctx
     * @return
     */
    private boolean checkRequestRatio(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        // 请求消息计数 ,300秒钟超过500条消息，断开连接 ，客户端登录时发送一堆请求消息
        Attribute<Integer> requestCountAttr = ctx.channel().attr(UserTcpServerHandler.REQUEST_COUNT);
        if (requestCountAttr == null) {
            LOGGER.error("用户：{}未正常进行初始化属性", MsgUtil.getIp(ctx.channel()));
            return false;
        }
        if (requestCountAttr.get() > MessageCheckCount) {
            long time = TimeUtil.currentTimeMillis();
            if (time - ctx.channel().attr(UserTcpServerHandler.REQUEST_RESET_TIME).get() < MessageCheckInterval) { // 不到5分钟请求了500个消息
                User userSession = ctx.channel().attr(UserTcpServerHandler.USER).get();
                LOGGER.warn("用户:{}-{}请求消息太频繁{}-->{}ms发送{}个消息", userSession.getAccount(), userSession.getPlayerId(),
                        MsgUtil.getRemoteIpPort(ctx.channel()),
                        time - ctx.channel().attr(UserTcpServerHandler.REQUEST_RESET_TIME).get(), MessageCheckCount);
                // 调试打印客户端发送的消息
                if (LOGGER.isDebugEnabled()) {
                    Map<Integer, Integer> msgIdMap = ctx.channel().attr(UserTcpServerHandler.REQUEST_MESSAGE_IDS).get();
                    msgIdMap.forEach((id, count) -> {
                        LOGGER.warn("{}-{} 消息{}:{}-->{}", userSession.getAccount(), userSession.getPlayerId(), id,
                                MID.forNumber(id), count);
                    });
                }
                ctx.channel().close();
                return false;
            }
            ctx.channel().attr(UserTcpServerHandler.REQUEST_COUNT).set(0);
            ctx.channel().attr(UserTcpServerHandler.REQUEST_RESET_TIME).set(time);
            if (LOGGER.isDebugEnabled()) {
                ctx.channel().attr(UserTcpServerHandler.REQUEST_MESSAGE_IDS).get().clear();
            }
        }
        requestCountAttr.set(requestCountAttr.get() + 1);

        // 调试打印客户端发送的消息
        if (LOGGER.isDebugEnabled()) {
            Map<Integer, Integer> msgIdMap = ctx.channel().attr(UserTcpServerHandler.REQUEST_MESSAGE_IDS).get();
            int msgId = byteBuf.getIntLE(4);
            Integer count = msgIdMap.get(msgId);
            if (count == null) {
                msgIdMap.put(msgId, 1);
            } else {
                msgIdMap.put(msgId, count + 1);
            }
        }
        return true;
    }

    @Override
    public void destroy() {
    }

}
