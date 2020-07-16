package org.mmo.gate.server.tcp.client.cluster;

import java.util.List;


import org.mmo.engine.io.message.IDMessage;
import org.mmo.engine.io.message.MsgType;
import org.mmo.engine.io.message.MsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

/**
 * 解析与cluster服连接的消息
 * 
 * @author JiangZhiYong
 */
@Deprecated
public class GateToClusterMessageCodec extends ByteToMessageCodec<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GateToClusterMessageCodec.class);
    /** 消息头长度，除去消息长度 消息类型2+玩家ID8 +消息ID4 */
    private static final int HEADER_EXCLUDE_LENGHT = 14;

    /**
     * 转发从用户及跨服到游戏服的消息
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

        // 需要组装玩家id，消息id等额外信息
        if (msg instanceof IDMessage) {
            IDMessage idMessage = (IDMessage)msg;
            if (idMessage.getMsg() instanceof byte[]) {
                byte[] bytes = (byte[])idMessage.getMsg();
                out.writeInt(HEADER_EXCLUDE_LENGHT + bytes.length);
                out.writeShort(MsgType.IDMESSAGE.getType());
                out.writeLong(idMessage.getId());
                out.writeInt(idMessage.getMsgId());
                out.writeBytes(bytes);
            } else if (idMessage.getMsg() instanceof Message) {
                Message message = (Message)idMessage.getMsg();
                byte[] bytes = message.toByteArray();
                out.writeInt(HEADER_EXCLUDE_LENGHT + bytes.length);
                out.writeShort(MsgType.IDMESSAGE.getType());
                out.writeLong(idMessage.getId());
                out.writeInt(idMessage.getMsgId());
                out.writeBytes(bytes);
            } else {
                LOGGER.warn("IDMessage加密类型{}未实现", idMessage.getMsg().getClass().getSimpleName());
            }

        } else {
            LOGGER.warn("加密类型{}未实现 {}", msg.getClass().getSimpleName(),msg.toString());
        }

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        MsgUtil.decode(ctx, in, out);
    }

}
