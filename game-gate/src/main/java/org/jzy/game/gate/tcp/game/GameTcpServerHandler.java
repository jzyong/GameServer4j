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
import org.jzy.game.common.constant.OfflineType;
import org.jzy.game.gate.struct.User;
import org.jzy.game.gate.service.GateManager;
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

    private TcpService tcpService;


    public GameTcpServerHandler(IExecutorService executorService) {
        this.executorService = executorService;
        this.tcpService = GateManager.getInstance().getGameTcpService();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LOGGER.info("{} 已打开连接", MsgUtil.getRemoteIpPort(ctx.channel()));
        tcpService.onChannelConnect(ctx.channel());
        ScriptManager.getInstance().consumerScript("GameChannelHandlerScript",
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //TODO 修改
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            int msgType = byteBuf.readShort();
            long id = byteBuf.readLong();
            if (msgType == 1) {// 数据结构:msgId:pfbytes
                int msgId = byteBuf.getInt(byteBuf.readerIndex());
                // 在本地注册，必须预处理
                var tcpHandlerBuilder = HandlerManager.getInstance().getTcpHandlerBuilder(msgId);
                //本地拦截处理
                if (tcpHandlerBuilder != null) {
                    byteBuf.readInt();  //删除消息id
                    byte[] bytes = new byte[byteBuf.readableBytes()];
                    byteBuf.readBytes(bytes);// 消息内容
                    Message message = tcpHandlerBuilder.buildMessage(bytes);
                    TcpHandler handler = (TcpHandler) tcpHandlerBuilder.buildHandler();
                    if (handler != null) {
                        handler.setId(id);
                        handler.setMsgBytes(bytes);
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
                        LOGGER.warn("玩家{} 已下线，消息{}-{}发送失败", id, msgId, MessageId.MID.forNumber(msgId).toString());
                        return;
                    }
                    user.sendToUser(bytes);
                }
            } else if (msgType == 2) {
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
