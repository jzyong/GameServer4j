package org.jzy.game.hall.server.client;

import com.google.protobuf.Message;
import com.jzy.javalib.network.io.handler.HandlerManager;
import com.jzy.javalib.network.io.handler.TcpHandler;
import com.jzy.javalib.network.netty.tcp.TcpClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.jzy.game.hall.service.HallManager;
import org.jzy.game.hall.struct.GateServerInfo;
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

    public GameToGateClientHandler() {
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.warn("已打开连接{}", ctx.channel());
        //TODO
//        ServerRegisterUpdateRequest request = ScriptManager.getInstance().consumerScript("GameServerScript", (IServerScript script) -> script.updateServerInfo());
//        MsgUtil.sendInnerMsg(ctx.channel(), request, -1, MessageId.MID.ServerRegisterUpdateReq_VALUE);
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
        Object paramObject = channel.attr(TcpClient.ChannelParamsKey).get();
        if (paramObject != null) {
            GateServerInfo gateServerInfo = HallManager.getInstance().getGateInfoService().deleteGateServer((String) paramObject);
            if (gateServerInfo != null) {
                LOGGER.warn("网关：{}-{}:{}移除", gateServerInfo.getId(), gateServerInfo.getIp(), gateServerInfo.getPort());
            }
        }
        //TODO 玩家离线处理
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //TODO
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            int msgType = byteBuf.readShort();
            long id = byteBuf.readLong();

            if (msgType == 1) {// 数据结构:msgId:pfbytes
                int msgId = byteBuf.readInt();
                byte[] bytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bytes);
                // 在本地注册，必须预处理
                var tcpHandlerBuilder = HandlerManager.getInstance().getTcpHandlerBuilder(msgId);
                if (tcpHandlerBuilder != null) {
                    Message message = tcpHandlerBuilder.buildMessage(bytes);
                    TcpHandler handler = (TcpHandler) tcpHandlerBuilder.buildHandler();
                    if (handler != null) {
                        handler.setId(id);
                        handler.setMsgBytes(bytes);
                        handler.setMessage(message);
                        handler.setChannel(ctx.channel());
                        Executor executor = HallManager.getInstance().getExecutorService().getExecutor(tcpHandlerBuilder.getExecuteThread());
                        if (executor == null) {
                            LOGGER.debug("handler:{} 指定线程{}未创建", handler.getClass().getSimpleName(), tcpHandlerBuilder.getExecuteThread());
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
