package org.jzy.game.gate.script.server;

import com.jzy.javalib.base.util.TimeUtil;
import com.jzy.javalib.network.io.message.MsgUtil;
import com.jzy.javalib.network.netty.IChannelHandlerScript;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.Attribute;
import io.netty.util.concurrent.ScheduledFuture;
import org.jzy.game.gate.tcp.user.UserTcpServerHandler;
import org.jzy.game.gate.service.GateManager;
import org.jzy.game.gate.struct.RC4;
import org.jzy.game.gate.struct.User;
import org.jzy.game.proto.MID;
import org.jzy.game.proto.MessageId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 游戏客户端用户netty连接
 *
 * @author JiangZhiYong
 */
public class UserChannelHandlerScript implements IChannelHandlerScript {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserChannelHandlerScript.class);

    /**
     * 使用消息压缩合并
     */
    public static final boolean MESSAGE_PACK = false;
    /**
     * 使用消息加密
     */
    public static final boolean MESSAGE_ENCRYPTION = false;

    /**
     * ip黑名单
     */
    private List<String> ipBlackList = new ArrayList<>();

    /**
     * key 加密对象
     */
    private RC4 keyEncryptRC4 = new RC4("f4426sdf2e-c27c-48f5-b0vb9-1mfd8d7d79b".getBytes());

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

        //属性设置
        User user = new User(channel);
        GateManager.getInstance().getUserService().onSocketConnect(user);
        Attribute<User> userAttr = channel.attr(UserTcpServerHandler.USER);
        userAttr.set(user);
        Attribute<Integer> requestCountAttr = channel.attr(UserTcpServerHandler.REQUEST_COUNT);
        requestCountAttr.set(0);
        Attribute<Long> requestResetTimeAttr = channel.attr(UserTcpServerHandler.REQUEST_RESET_TIME);
        requestResetTimeAttr.set(TimeUtil.currentTimeMillis());
        //统计请求消息id
        if (LOGGER.isDebugEnabled()) {
            Attribute<Map<Integer, Integer>> requestMessageIdAttr = channel.attr(UserTcpServerHandler.REQUEST_MESSAGE_IDS);
            requestMessageIdAttr.set(new HashMap<>());
        }


        // 消息打包发送
        if (MESSAGE_PACK) {
            // 发送超时消息
            ScheduledFuture<?> scheduledFuture = ctx.channel().eventLoop().scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    if (user.getPackMessages().size() > 0) {
                        user.sendToUser();
                    }
                }
            }, 1000L, 70L, TimeUnit.MILLISECONDS);//1秒30帧
            user.setPackMessageFuture(scheduledFuture);
        }

        //请求消息包加密
        if (MESSAGE_ENCRYPTION) {
            byte[] encryptKey = RC4.getRandomKey();
            RC4 rc4 = new RC4(encryptKey);
            user.setRc4(rc4);
            //发送密钥给客户端
            ByteBuf buf = Unpooled.buffer(8 + encryptKey.length);
            buf.writeInt(4 + encryptKey.length);
            buf.writeInt(0); //TODO 消息id再协商
//            LOGGER.info("原始密钥：{} -->{}", ByteUtil.bytesToHex(encryptKey),ByteUtil.bytesToHex("f4426b2e-c27c-48f5-b0a9-1744d8d7d79b".getBytes()));
            byte[] encryptBytes = Arrays.copyOf(encryptKey, encryptKey.length);
            keyEncryptRC4.crypt(encryptBytes, 0, -1);
//            LOGGER.info("加密密钥：{}", ByteUtil.bytesToHex(encryptBytes));
            buf.writeBytes(encryptBytes);
            ctx.writeAndFlush(buf);

        } else {
            ByteBuf buf = Unpooled.buffer(8);
            buf.writeInt(4);
            buf.writeInt(0);
            ctx.writeAndFlush(buf);
        }

        //添加返回消息统计
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
        // 请求消息计数 ,10秒钟超过100条消息，断开连接
        Attribute<Integer> requestCountAttr = ctx.channel().attr(UserTcpServerHandler.REQUEST_COUNT);
        if (requestCountAttr == null) {
            LOGGER.error("用户：{}未正常进行初始化属性", MsgUtil.getIp(ctx.channel()));
            return false;
        }
        if (requestCountAttr.get() > 300) {
            long time = TimeUtil.currentTimeMillis();
            if (time - ctx.channel().attr(UserTcpServerHandler.REQUEST_RESET_TIME).get() < 10000) {
                User userSession = ctx.channel().attr(UserTcpServerHandler.USER).get();
                LOGGER.warn("用户:{}-{}请求消息太频繁{}-->{}ms发送300个消息", userSession.getAccount(), userSession.getUserId(),
                        MsgUtil.getRemoteIpPort(ctx.channel()), time - ctx.channel().attr(UserTcpServerHandler.REQUEST_RESET_TIME).get());
                //调试打印客户端发送的消息
                if (LOGGER.isDebugEnabled()) {
                    Map<Integer, Integer> msgIdMap = ctx.channel().attr(UserTcpServerHandler.REQUEST_MESSAGE_IDS).get();
                    msgIdMap.forEach((id, count) -> {
                        LOGGER.warn("{} 消息{}-{}", userSession.getAccount(), MID.forNumber(id), count);
                    });
                }
                String ip = MsgUtil.getIp(ctx.channel());
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

        //调试打印客户端发送的消息
        if (LOGGER.isDebugEnabled()) {
            Map<Integer, Integer> msgIdMap = ctx.channel().attr(UserTcpServerHandler.REQUEST_MESSAGE_IDS).get();
            int msgId = byteBuf.getInt(0);
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
