package org.jzy.game.gate.tcp.user;

import com.google.protobuf.Message;
import com.jzy.javalib.base.script.ScriptManager;
import com.jzy.javalib.network.io.message.MsgUtil;
import com.jzy.javalib.network.netty.IChannelHandlerScript;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.jzy.game.proto.MID;
import org.jzy.game.proto.MessageId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 解析与客户端连接的消息
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public class UserTcpByteToMessageCodec extends ByteToMessageCodec<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserTcpByteToMessageCodec.class);

    public UserTcpByteToMessageCodec() {

    }

    /**
     * 消息长度4+消息id4+客户端已收到最小序号4+消息序号4+protobuf消息体
     *
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof byte[]) {
            //包含了消息id
            byte[] bytes = (byte[]) msg;
            out.writeInt(bytes.length);
            if (bytes.length > MsgUtil.MESSAGE_MAX_SIZE) {
                LOGGER.warn("消息：{} 拥有{}字节，将拆包发送", MID.forNumber(MsgUtil.getMessageID(bytes, 0)), bytes.length);
            }
            // 消息id+消息内容
            out.writeBytes(bytes);
        } else if (msg instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf) msg;
            if (byteBuf.readableBytes() > MsgUtil.MESSAGE_MAX_SIZE) {
                LOGGER.warn("向{} 一次性发送{}  {} 字节", MsgUtil.getLocalIpPort(ctx.channel()), MID.forNumber(byteBuf.getIntLE(4)), byteBuf.readableBytes());
            }
            // LOGGER.debug("消息长度{} 消息id：{}",byteBuf.getIntLE(0),byteBuf.getIntLE(4));
            out.writeBytes(byteBuf);
        } else if (msg instanceof Message) {  //此发送方式不推荐，不能返回消息序号
            Message message = (Message) msg;
            try {
                int messageID = MsgUtil.getMessageID(message);
                byte[] bytes = message.toByteArray();
                if (bytes.length > MsgUtil.MESSAGE_MAX_SIZE) {
                    LOGGER.warn("消息：{} 拥有{}字节，将拆包发送", MID.forNumber(messageID), bytes.length);
                }
                out.writeIntLE(bytes.length + 12); // 消息id+消息内容长度
                out.writeIntLE(messageID);
                out.writeIntLE(0); //保留字段
                out.writeIntLE(0); //此次不能获取消息序号，不要发送message
                out.writeBytes(bytes);
            } catch (Exception e) {
                String className = message.getClass().getSimpleName();
                LOGGER.error(String.format("获取消息id异常:%s ,请按标准定义协议id，协议ID未消息类目去掉最后五个字符", className), e);
            }
        } else {
            LOGGER.warn("加密类型{}未实现", msg.getClass().getSimpleName());
        }

    }

    //消息长度4+消息id4+客户端已收到最小序号4+消息序号4+protobuf消息体
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readIntLE();
//        int dataLength=in.getIntLE(0);

        if (dataLength < 1) {
            LOGGER.warn("消息解析异常,长度{}，id{}", dataLength, in.readInt());
            in.clear();
            ctx.close();
            return;
        }

        dataLength = dataLength & 0xFFFFF;

        // 消息体长度不够，继续等待
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        in.resetReaderIndex();
        ByteBuf readRetainedSlice = in.readRetainedSlice(dataLength + 4);
        if (!ScriptManager.getInstance().functionScript("UserChannelHandlerScript",
                (IChannelHandlerScript script) -> script.inBoundMessageCheck(ctx, readRetainedSlice))) {
            return;
        }
        out.add(readRetainedSlice);
    }

}

