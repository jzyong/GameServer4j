package org.mmo.gate.tcp.user;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.mmo.engine.io.message.MsgUtil;
import org.mmo.engine.io.netty.script.IChannelHandlerScript;
import org.mmo.engine.script.ScriptService;
import org.mmo.message.MIDMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 解析与客户端连接的消息
 * 
 * @author JiangZhiYong
 */
public class UserTcpByteToMessageCodec extends ByteToMessageCodec<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserTcpByteToMessageCodec.class);
    private ScriptService scriptService;

    public UserTcpByteToMessageCodec(ScriptService scriptService){
        this.scriptService=scriptService;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof byte[]) {
            //包含了消息id
            byte[] bytes = (byte[]) msg;
            out.writeInt(bytes.length);
            if (bytes.length > MsgUtil.MESSAGE_MAX_SIZE) {
                LOGGER.warn("消息：{} 拥有{}字节，将拆包发送", MIDMessage.MID.forNumber(MsgUtil.getMessageID(bytes, 0)), bytes.length);
            }
            // 消息id+消息内容
            out.writeBytes(bytes);
        }else if (msg instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf) msg;
            if (byteBuf.readableBytes() > MsgUtil.MESSAGE_MAX_SIZE) {
                LOGGER.warn("向{} 一次性发送 {} 字节", MsgUtil.getLocalIpPort(ctx.channel()), byteBuf.readableBytes());
            }
            out.writeBytes(byteBuf);
        }else if(msg instanceof Message){
            Message message = (Message) msg;
            String className=message.getClass().getSimpleName();
            try {
                int messageID = MIDMessage.MID.valueOf(className.substring(0,className.length()-5)).getNumber();

                byte[] bytes = message.toByteArray();
                if (bytes.length > MsgUtil.MESSAGE_MAX_SIZE) {
                    LOGGER.warn("消息：{} 拥有{}字节，将拆包发送", MIDMessage.MID.forNumber(messageID), bytes.length);
                }
                out.writeInt(bytes.length + 4); // 消息id+消息内容长度
                out.writeInt(messageID);
                out.writeBytes(bytes);
            }catch (Exception e){
                LOGGER.error(String.format("获取消息id异常:%s ,请按标准定义协议id，协议ID未消息类目去掉最后五个字符",className),e);
            }
        } else {
            LOGGER.warn("加密类型{}未实现", msg.getClass().getSimpleName());
        }

    }

    //消息长度（4）+消息id（4）+消息体
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();

        if (dataLength < 1) {
            LOGGER.warn("消息解析异常,长度{}，id{}", dataLength, in.readInt());
            in.clear();
            ctx.close();
            return;
        }

        // 消息体长度不够，继续等待
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }

        ByteBuf readRetainedSlice = in.readRetainedSlice(dataLength);
        if (!scriptService.functionScript("UserChannelHandlerScript",
                (IChannelHandlerScript script) -> script.inBoundMessageCheck(ctx,readRetainedSlice))) {
            return;
        }

        out.add(readRetainedSlice);
    }

}
