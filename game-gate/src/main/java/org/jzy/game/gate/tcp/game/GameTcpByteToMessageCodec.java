package org.jzy.game.gate.tcp.game;

import com.google.protobuf.Message;
import com.jzy.javalib.network.io.message.IdMessage;
import com.jzy.javalib.network.io.message.MsgUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 解析与游戏连接的消息
 *
 * @author JiangZhiYong
 */
public class GameTcpByteToMessageCodec extends ByteToMessageCodec<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameTcpByteToMessageCodec.class);
    /**
     * 消息头长度，除去消息长度
     */
    public static final int HEADER_EXCLUDE_LENGTH = 16;

    public GameTcpByteToMessageCodec() {

    }

    //消息长度4+消息id4+玩家id8+消息序列号4+protobuf消息体
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

        // 需要组装玩家id，消息id等额外信息
        if (msg instanceof IdMessage) {
            IdMessage idMessage = (IdMessage) msg;
            if (idMessage.getMsg() instanceof byte[]) {
                byte[] bytes = (byte[]) idMessage.getMsg();
                out.writeInt(HEADER_EXCLUDE_LENGTH + bytes.length);
                out.writeInt(idMessage.getMsgId());
                out.writeLong(idMessage.getId());
                out.writeInt(idMessage.getMsgSequence());
                out.writeBytes(bytes);
            } else if (idMessage.getMsg() instanceof Message) {
                Message message = (Message) idMessage.getMsg();
                byte[] bytes = message.toByteArray();
                out.writeInt(HEADER_EXCLUDE_LENGTH + bytes.length);
                out.writeInt(idMessage.getMsgId());
                out.writeLong(idMessage.getId());
                out.writeInt(idMessage.getMsgSequence());
                out.writeBytes(bytes);
            } else {
                LOGGER.warn("IdMessage加密类型{}未实现", idMessage.getMsg().getClass().getSimpleName());
            }
        } else {
            LOGGER.warn("加密类型{}未实现", msg.getClass().getSimpleName());
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        MsgUtil.decode(ctx, in, out);
    }
}
