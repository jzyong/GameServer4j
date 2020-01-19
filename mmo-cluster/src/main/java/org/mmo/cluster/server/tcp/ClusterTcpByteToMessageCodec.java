package org.mmo.cluster.server.tcp;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.mmo.engine.io.message.IDMessage;
import org.mmo.engine.io.message.MsgType;
import org.mmo.engine.io.message.MsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 解析与游戏服连接的消息
 * 
 * @author JiangZhiYong
 */
public class ClusterTcpByteToMessageCodec extends ByteToMessageCodec<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterTcpByteToMessageCodec.class);
    /** 消息头长度，除去消息长度 消息类型2+消息id4+唯一ID8 */
    private static final int HEADER_EXCLUDE_LENGTH = 14;

    /**
     * 转发从用户及跨服到游戏服的消息
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

        // 需要组装玩家id，消息id等额外信息
        if (msg instanceof IDMessage) {
            IDMessage idMessage = (IDMessage)msg;
            if (idMessage.getMsg() instanceof byte[]) {
                byte[] bytes = (byte[])idMessage.getMsg();
                out.writeInt(HEADER_EXCLUDE_LENGTH + bytes.length);
                out.writeShort(MsgType.IDMESSAGE.getType());
                out.writeLong(idMessage.getId());
                out.writeInt(idMessage.getMsgId());
                out.writeBytes(bytes);
            } else if (idMessage.getMsg() instanceof Message) {
                Message message = (Message)idMessage.getMsg();
                byte[] bytes = message.toByteArray();
                out.writeInt(HEADER_EXCLUDE_LENGTH + bytes.length);
                out.writeShort(MsgType.IDMESSAGE.getType());
                out.writeLong(idMessage.getId());
                out.writeInt(idMessage.getMsgId());
                out.writeBytes(bytes);
            } else {
                LOGGER.warn("IDMessage加密类型{}未实现", idMessage.getMsg().getClass().getSimpleName());
            }

        } else {
            LOGGER.warn("加密类型{}未实现", msg.getClass().getSimpleName());
        }

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        MsgUtil.decode(ctx, in, out);
    }

}
