package org.mmo.gate.server.tcp.server.game;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import org.mmo.common.constant.OfflineType;
import org.mmo.engine.io.handler.TcpHandler;
import org.mmo.engine.io.message.MsgType;
import org.mmo.engine.io.message.MsgUtil;
import org.mmo.engine.io.message.TcpMessageBean;
import org.mmo.engine.io.netty.script.IChannelHandlerScript;
import org.mmo.engine.io.service.TcpService;
import org.mmo.engine.script.ScriptService;
import org.mmo.engine.thread.IExecutorService;
import org.mmo.engine.util.TimeUtil;
import org.mmo.gate.service.GateManager;
import org.mmo.gate.struct.User;
import org.mmo.message.MIDMessage;
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
public class GameTcpServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameTcpServerHandler.class);

    private ScriptService scriptService;

    private IExecutorService executorService;

    private TcpService tcpService;


    public GameTcpServerHandler(ScriptService scriptService, IExecutorService executorService, TcpService tcpService) {
        this.executorService = executorService;
        this.scriptService = scriptService;
        this.tcpService = tcpService;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LOGGER.info("{} 已打开连接", MsgUtil.getRemoteIpPort(ctx.channel()));
        tcpService.onChannelConnect(ctx.channel());
        scriptService.consumerScript("GameChannelHandlerScript",
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
        //TODO 踢下所有连接该游戏服的角色
    }


    @SuppressWarnings("rawtypes")
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            int msgType = byteBuf.readShort();
            long id = byteBuf.readLong();
            if (msgType == MsgType.IDMESSAGE.getType()) {// 数据结构:msgId:pfbytes
                int msgId = byteBuf.getInt(byteBuf.readerIndex());
                // 在本地注册，必须预处理
                TcpMessageBean messageBean = scriptService.getMessagebean(byteBuf.readInt());
                byte[] bytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bytes);// 消息id+消息内容
                //本地拦截处理
                if (messageBean != null) {
                    Message message = messageBean.buildMessage(bytes);
                    TcpHandler handler = (TcpHandler) messageBean.newHandler();
                    if (handler != null) {
                        handler.setPid(id);
                        handler.setMsgBytes(bytes);
                        handler.setMessage(message);
                        handler.setChannel(ctx.channel());
                        Executor executor = executorService.getExecutor(messageBean.getExecuteThread());
                        executor.execute(handler);
                        return;
                    }
                } else {
                    // 转发给客户端
                    User user = GateManager.getInstance().getUserService().getUserByPlayerId(id);
                    if (user == null) {
                        LOGGER.warn("玩家{} 已下线，消息{}-{}发送失败", id, msgId, MIDMessage.MID.forNumber(msgId).toString());
                        return;
                    }
                    user.sendToUser(bytes);
                }
            } else if (msgType == MsgType.CROSSMESSAGE.getType()) {
                // note 暂时无跨服需求
            } else {
                LOGGER.warn("消息类型{}未实现,玩家{}消息发送失败", msgType, id);
            }
        } catch (Exception e) {
            LOGGER.error("消息处理", e);
        } finally {
            byteBuf.release();
        }
    }

}
