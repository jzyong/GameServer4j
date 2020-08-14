package org.mmo.gate.server.tcp.server.game;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.mmo.engine.io.message.IdMessage;
import org.mmo.engine.io.message.MsgType;
import org.mmo.engine.io.message.MsgUtil;
import org.mmo.engine.io.netty.script.IChannelHandlerScript;
import org.mmo.engine.script.ScriptService;
import org.mmo.message.MIDMessage;
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
    private ScriptService scriptService;
    /**
     * 消息头长度，除去消息长度 消息类型2+玩家ID8+消息id
     */
    public static final int HEADER_EXCLUDE_LENGTH = 14;

    public GameTcpByteToMessageCodec(ScriptService scriptService) {
        this.scriptService = scriptService;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

        // 需要组装玩家id，消息id等额外信息
        if (msg instanceof IdMessage) {
            IdMessage idMessage = (IdMessage) msg;
            if (idMessage.getMsg() instanceof byte[]) {
                byte[] bytes = (byte[]) idMessage.getMsg();
                out.writeInt(HEADER_EXCLUDE_LENGTH + bytes.length);
                out.writeShort(MsgType.IDMESSAGE.getType());
                out.writeLong(idMessage.getId());
                out.writeInt(idMessage.getMsgId());
                out.writeBytes(bytes);
            } else if (idMessage.getMsg() instanceof Message) {
                Message message = (Message) idMessage.getMsg();
                byte[] bytes = message.toByteArray();
                out.writeInt(HEADER_EXCLUDE_LENGTH + bytes.length);
                out.writeShort(MsgType.IDMESSAGE.getType());
                out.writeLong(idMessage.getId());
                out.writeInt(idMessage.getMsgId());
                out.writeBytes(bytes);
            } else {
                LOGGER.warn("IdMessage加密类型{}未实现", idMessage.getMsg().getClass().getSimpleName());
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
