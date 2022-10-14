package org.jzy.game.gate.tcp.game;

import com.google.protobuf.Message;
import com.jzy.javalib.base.script.ScriptManager;
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
import io.netty.util.AttributeKey;
import org.jzy.game.common.constant.OfflineType;
import org.jzy.game.gate.struct.User;
import org.jzy.game.gate.service.GateManager;
import org.jzy.game.proto.MID;
import org.jzy.game.proto.MessageId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * 登录服tcp消息处理
 *
 * @author JiangZhiYong
 * @date 2018/12/11
 */
public class GameTcpServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameTcpServerHandler.class);


    private IExecutorService executorService;

    /**
     * 游戏服务器信息
     */
    public static final AttributeKey<GameServerInfo> GameServerInfo = AttributeKey.valueOf("GameServerInfo");

    public GameTcpServerHandler(IExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LOGGER.info("{} 已打开连接", MsgUtil.getRemoteIpPort(ctx.channel()));
        GateManager.getInstance().getGameTcpService().onChannelConnect(ctx.channel());
        ScriptManager.getInstance().consumerScript("GameChannelHandlerScript",
                (IChannelHandlerScript script) -> script.channelActive(ctx));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx)  {
        channelClosed(ctx.channel(), OfflineType.Network);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause.getMessage().contains("Connection reset by peer")
                || cause.getMessage().contains("远程主机强迫关闭了一个现有的连接")) {
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
        GateManager.getInstance().getGameTcpService().onChannelClosed(channel);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            int msgId = byteBuf.readInt();
            long id = byteBuf.readLong();
            int msgSequence = byteBuf.readInt();
            if (LOGGER.isTraceEnabled() && msgId != MID.HeartReq_VALUE) {
                LOGGER.debug("返回消息：{}-{}", msgId, MID.forNumber(msgId));
            }
            // 在本地注册，必须预处理
            var tcpHandlerBuilder = HandlerManager.getInstance().getTcpHandlerBuilder(msgId);
            // 本地拦截处理
            if (tcpHandlerBuilder != null) {
                byte[] bytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bytes);// 消息内容
                Message message = tcpHandlerBuilder.buildMessage(bytes);
                TcpHandler handler = (TcpHandler) tcpHandlerBuilder.buildHandler();
                if (handler != null) {
                    handler.setId(id);
                    handler.setMsgBytes(bytes);
                    handler.setMsgSequence(msgSequence);
                    handler.setMessage(message);
                    handler.setChannel(ctx.channel());
                    Executor executor = executorService.getExecutor(tcpHandlerBuilder.getExecuteThread());
                    executor.execute(handler);
                    return;
                }
            } else {
                byte[] bytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bytes);// 消息id+消息内容
                // 转发给客户端
                User user = GateManager.getInstance().getUserService().getUserByPlayerId(id);
                if (user == null) {
                    return;
                }
                //返回长度+16（消息头）
                LOGGER.debug("{} 返回序号{} 确认0 协议{}-{} 长度{}", id, msgSequence, msgId,MID.forNumber(msgId),bytes.length+16);
                user.receiveUserMessage(msgId, msgSequence, bytes);
            }
        } catch (Exception e) {
            LOGGER.error("消息处理", e);
        } finally {
            byteBuf.release();
        }
    }

}
