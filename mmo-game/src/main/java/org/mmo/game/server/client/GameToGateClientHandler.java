package org.mmo.game.server.client;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.mmo.common.scripts.IServerScript;
import org.mmo.engine.io.handler.TcpHandler;
import org.mmo.engine.io.message.MsgType;
import org.mmo.engine.io.message.MsgUtil;
import org.mmo.engine.io.message.TcpMessageBean;
import org.mmo.game.service.GameManager;
import org.mmo.game.struct.GateServerInfo;
import org.mmo.message.MIDMessage;
import org.mmo.message.ServerRegisterUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * 处理cluster返回消息 <br>
 * 待修改
 *
 * @author JiangZhiYong
 */
public class GameToGateClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameToGateClientHandler.class);
    public static final AttributeKey<Integer> ServerId = AttributeKey.valueOf("SERVER_ID");

    public GameToGateClientHandler() {
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.warn("已打开连接{}", ctx.channel());
        ServerRegisterUpdateRequest request = GameManager.getInstance().getScriptService().functionScript("GameServerScript", (IServerScript script) -> script.buildServerRegisterUpdateRequest());
        MsgUtil.sendInnerMsg(ctx.channel(), request, -1, MIDMessage.MID.ServerRegisterUpdateReq_VALUE);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channelClosed(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.warn("连接{} {}【异常关闭】", ctx.channel().remoteAddress(), cause.getMessage());
        channelClosed(ctx.channel());
    }

    public void channelClosed(Channel channel) {
        LOGGER.warn("连接{}已关闭", channel);
        // 移除网关服务器
        Integer serverId = channel.attr(ServerId).get();
        if (serverId != null) {
            GateServerInfo gateServerInfo = GameManager.getInstance().getGameToClusterRpcService().getGateServerInfoMap().remove(serverId);
            if (gateServerInfo != null) {
                LOGGER.warn("网关：{}-{}-{}移除", gateServerInfo.getServerInfo().getId(), gateServerInfo.getServerInfo().getName(), gateServerInfo.getServerInfo().getIp());
            }
        }
        //TODO 玩家离线处理
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            int msgType = byteBuf.readShort();
            long id = byteBuf.readLong();

            if (msgType == MsgType.IDMESSAGE.getType()) {// 数据结构:msgId:pfbytes
                int msgId = byteBuf.readInt();
                byte[] bytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bytes);
                // 在本地注册，必须预处理
                TcpMessageBean messageBean = GameManager.getInstance().getScriptService().getMessagebean(msgId);
                if (messageBean != null) {
                    Message message = messageBean.buildMessage(bytes);
                    TcpHandler handler = (TcpHandler) messageBean.newHandler();
                    if (handler != null) {
                        handler.setPid(id);
                        handler.setMsgBytes(bytes);
                        handler.setMessage(message);
                        handler.setChannel(ctx.channel());
                        Executor executor = GameManager.getInstance().getExecutorService().getExecutor(messageBean.getExecuteThread());
                        if (executor == null) {
                            LOGGER.debug("handler:{} 指定线程{}未创建", handler.getClass().getSimpleName(), messageBean.getExecuteThread());
                            handler.run();
                            return;
                        }
                        executor.execute(handler);
                    }
                } else {
                    LOGGER.warn("消息[{}]代码未实现逻辑", msgId);
                }
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
