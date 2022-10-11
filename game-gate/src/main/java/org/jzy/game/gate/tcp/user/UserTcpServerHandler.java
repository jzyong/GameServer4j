package org.jzy.game.gate.tcp.user;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.jzy.javalib.base.script.ScriptManager;
import com.jzy.javalib.base.util.TimeUtil;
import com.jzy.javalib.network.io.handler.HandlerManager;
import com.jzy.javalib.network.io.handler.TcpHandler;
import com.jzy.javalib.network.io.message.MsgUtil;
import com.jzy.javalib.network.netty.IChannelHandlerScript;
import com.jzy.javalib.network.netty.tcp.TcpService;
import com.jzy.javalib.network.scene.IExecutorService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import org.jzy.game.common.constant.OfflineType;
import org.jzy.game.gate.service.GateManager;
import org.jzy.game.gate.struct.User;
import org.jzy.game.proto.MessageId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 登录服tcp消息处理
 *
 * @author JiangZhiYong
 * @date 2018/12/11
 */
public class UserTcpServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserTcpServerHandler.class);


    private IExecutorService executorService;

    private TcpService tcpService;

    public static final AttributeKey<User> USER = AttributeKey.valueOf("User");
    /**
     * 请求消息计数
     */
    public static final AttributeKey<Integer> REQUEST_COUNT = AttributeKey.valueOf("RequestCount");
    /**
     * 请求消息统计
     */
    public static final AttributeKey<Map<Integer, Integer>> REQUEST_MESSAGE_IDS = AttributeKey
            .valueOf("RequestMessageIds");
    /**
     * 请求消息时间重置
     */
    public static final AttributeKey<Long> REQUEST_RESET_TIME = AttributeKey.valueOf("RequestResetTime");

    /**
     * 返回消息计数
     */
    public static final AttributeKey<Integer> RESPONSE_COUNT = AttributeKey.valueOf("ResponseCount");
    /**
     * 返回消息统计
     */
    public static final AttributeKey<Map<Integer, Integer>> RESPONSE_MESSAGE_IDS = AttributeKey
            .valueOf("ResponseMessageIds");
    /**
     * 返回消息时间重置
     */
    public static final AttributeKey<Long> RESPONSE_RESET_TIME = AttributeKey.valueOf("ResponseResetTime");

    public UserTcpServerHandler(IExecutorService executorService) {
        this.executorService = executorService;
        this.tcpService = GateManager.getInstance().getUserTcpService();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        LOGGER.info("{} 已打开连接", MsgUtil.getRemoteIpPort(channel));
        tcpService.onChannelConnect(ctx.channel());
        boolean blackList = ScriptManager.getInstance().functionScript("UserChannelHandlerScript",
                (IChannelHandlerScript script) -> script.isBlackList(ctx));
        if (blackList) {
            LOGGER.warn("客户端：{}被ip限制", MsgUtil.getIp(ctx.channel()));
            channel.close();
        }
        ScriptManager.getInstance().consumerScript("UserChannelHandlerScript",
                (IChannelHandlerScript script) -> script.channelActive(ctx));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channelClosed(ctx.channel(), OfflineType.Network);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause.getMessage().contains("Connection reset by peer") || cause.getMessage().contains("远程主机强迫关闭了一个现有的连接")) {
            channelClosed(ctx.channel(), OfflineType.Network);
        } else {
            LOGGER.warn("连接{} 异常关闭 -->{} ", ctx.channel().remoteAddress(), cause.getMessage());
            LOGGER.warn("exceptionCaught ", cause);
            channelClosed(ctx.channel(), OfflineType.Exception);
        }
    }

    /**
     * 关闭网络
     *
     * @param channel
     * @param offlineType
     */
    private void channelClosed(Channel channel, OfflineType offlineType) {
        tcpService.onChannelClosed(channel);
        GateManager.getInstance().getUserService().offLine(channel, offlineType);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            User user = ctx.channel().attr(USER).get();
            // 客户端为发消息，断开连接
            if (e.state() == IdleState.READER_IDLE) {
                channelClosed(ctx.channel(), OfflineType.Idle);
            } else if (e.state() == IdleState.WRITER_IDLE) {
                LOGGER.warn("[{}-{}]连接超两分钟未收到任何服务器消息", user.getAccount(), user.getUserId());
            }
        }
    }


    @SuppressWarnings("rawtypes")
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        User user = null;
        int mid = 0;
        try {
            mid = byteBuf.readInt();
            user = ctx.channel().attr(USER).get();
            if (user == null) {
                LOGGER.warn("{}请求消息{}用户未登录", MsgUtil.getRemoteIpPort(ctx.channel()), MessageId.MID.forNumber(mid));
                channelClosed(ctx.channel(), OfflineType.IllegalRequest);
                return;
            }
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            //解码
            if (user.getRc4() != null) {
                user.getRc4().crypt(bytes, 0, -1);
            }

            // 消息在网关服注册
            var tcpHandlerBuilder = HandlerManager.getInstance().getTcpHandlerBuilder(mid);
            if (tcpHandlerBuilder != null) {
                Message message = tcpHandlerBuilder.buildMessage(bytes);
                TcpHandler handler = (TcpHandler) tcpHandlerBuilder.buildHandler();
                if (handler != null) {
                    handler.setMessage(message);
                    handler.setMsgBytes(bytes);
                    handler.setChannel(ctx.channel());
                    handler.setCreateTime(TimeUtil.currentTimeMillis());
                    handler.setId(user.getPlayerId() > 0 ? user.getPlayerId() : user.getUserId());
                    Executor executor = executorService.getExecutor(tcpHandlerBuilder.getExecuteThread());
                    executor.execute(handler);
                    return;
                }
            } else {
                // 直接转发
                user.sendToGame(bytes, mid);
            }
        } catch (Exception e) {
            if (e instanceof InvalidProtocolBufferException) {
                String ip = MsgUtil.getIp(ctx.channel());
                LOGGER.warn("{}-{}-{} 发送非法消息", user.getUserId(), user.getPlayerId(), ip);
            }
            if (mid > 0) {
                LOGGER.error("消息{} {} 解析错误", mid, MessageId.MID.forNumber(mid));
            }
            LOGGER.error("网络消息", e);
        } finally {
            byteBuf.release();
        }
    }

}
