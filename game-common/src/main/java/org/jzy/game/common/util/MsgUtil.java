package org.jzy.game.common.util;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

/**
 * @author jzyong
 * @mail 359135103@qq.com
 */
@SuppressWarnings("deprecation")
public class MsgUtil {

	protected static Logger LOGGER = LoggerFactory.getLogger(MsgUtil.class);

	/**
	 * 消息期待大小，mtu一般为1500，如果大于该值会分包发送
	 */
	public static final int MESSAGE_EXPIRE_SIZE = 1300;
	public static final int MESSAGE_MTU_SIZE = 1300;
	/**
	 * 客户端最大处理消息为50K,超过将抛出异常，服务器压测显示最大消息包为20K（进入地图待优化）
	 */
	public static final int MESSAGE_MAX_SIZE = 35000;
	/**
	 * 消息ID长度
	 */
	public static final int MSG_ID_LENGTH = 4;
	/**
	 * 消息PID长度
	 */
	public static final int MSG_PID_LENGTH = 8;
	/**
	 * 内部消息类型
	 */
	public static final int MSG_INNER_TYPE = 2;

	/**
	 * 客户端消息头长度
	 */
	public static final int ClientHeaderLength = 16;
	/**
	 * 客户端消息长度，不包括自身
	 */
	public static final int ClientHeaderExcludeLength = 12;

	/**
	 * 发送内部消息 IDMessage
	 *
	 * @param channel
	 * @param message
	 * @param playerId
	 * @return
	 */
	public final static boolean sendInnerMsg(Channel channel, Message message, long playerId, int messageId,
			int msgSequence) {
		if (message == null) {
			return false;
		}
		try {
			if (channel != null && channel.isActive()) {
				channel.writeAndFlush(IdMessage.newIDMessage(channel, message, playerId, messageId, msgSequence));
				return true;
			} else {
				LOGGER.warn("发送消息失败{}，连接异常", message.getClass().getName());
			}
		} catch (Exception e) {
			LOGGER.error("sendInnerMsg", e);
		}
		return false;
	}

	/**
	 * 发送内部消息 IDMessage
	 *
	 * @param channel
	 * @param message
	 * @param playerId
	 * @return
	 */
	public final static boolean sendInnerMsg(Channel channel, Message message, long playerId, int msgSequence) {
		if (message == null) {
			return false;
		}
		int messageId = getMessageID(message);
		return sendInnerMsg(channel, message, playerId, messageId, msgSequence);
	}

	/**
	 * 消息ID
	 *
	 * @param message
	 * @return
	 */
	public static int getMessageID(final Message message) {
		Descriptors.EnumValueDescriptor field = (Descriptors.EnumValueDescriptor) message
				.getField(message.getDescriptorForType().findFieldByNumber(1));
		int msgID = field.getNumber();
		return msgID;
	}

	/**
	 * 发送内部消息 IDMessage
	 *
	 * @param channel
	 * @param msg
	 * @return
	 */
	public final static boolean sendInnerMsg(Channel channel, Object msg) {
		if (msg == null) {
			return false;
		}
		if (channel != null && channel.isActive()) {
			if (msg instanceof IdMessage) {
				channel.writeAndFlush(msg);
			} else {
				throw new UnsupportedOperationException(String.format("消息 %s 类型不支持", msg.getClass().getSimpleName()));
			}
			return true;
		} else {
			LOGGER.warn("发送消息失败{}，连接异常", msg.getClass().getName());
		}
		return false;
	}

	/**
	 * 返回客户端数据，没有序列号
	 * 
	 * @param channel
	 * @param msg
	 * @return
	 */
	public final static boolean sendClientMsg(Channel channel, Object msg) {
		return sendClientMsg(channel, msg, 0);
	}

	/**
	 * 直接发送
	 *
	 * @param channel
	 * @param msg
	 * @return
	 */
	public final static boolean sendClientMsg(Channel channel, Object msg, int msgSequence) {
		if (msg == null) {
			return false;
		}
		if (channel != null && channel.isActive()) {
			// 不能直接发生message，需要转换
			if (msgSequence > 0 && msg instanceof Message) {
				var message = (Message) msg;
				var bytes = message.toByteArray();
				// 消息长度4+消息id4+保留字段4+消息序号4+protobuf消息体
				ByteBuf buffer = Unpooled.buffer(ClientHeaderLength + bytes.length);
				buffer.writeIntLE(ClientHeaderExcludeLength + bytes.length);
				buffer.writeIntLE(getMessageID(message));
				buffer.writeIntLE(0);
				buffer.writeIntLE(msgSequence);
				buffer.writeBytes(bytes);
				channel.writeAndFlush(buffer);
			} else {
				channel.writeAndFlush(msg);
			}
			return true;
		} else {
			if (msg instanceof ByteBuf) {
				LOGGER.info("发送消息:{}失败，连接断开", ((ByteBuf) msg).getIntLE(4));
			} else {
				LOGGER.info("发送消息失败{}，连接异常", msg.getClass().getName());
			}
		}
		return false;
	}

	/**
	 * 给客户端发送加密消息
	 * 
	 * @param channel
	 * @param msg
	 * @param msgSequence
	 * @param msgId
	 * @return
	 */
	public static boolean sendClientEncryptMsg(Channel channel, byte[] msg, int msgSequence, int msgId) {
		if (msg == null) {
			return false;
		}
		if (channel != null && channel.isActive()) {
			// 不能直接发生message，需要转换
			// 消息长度4+消息id4+保留字段4+消息序号4+protobuf消息体
			ByteBuf buffer = Unpooled.buffer(ClientHeaderLength + msg.length);
			int length = ClientHeaderExcludeLength + msg.length;
			buffer.writeIntLE(length | 0x40000000);
			buffer.writeIntLE(msgId);
			buffer.writeIntLE(0);
			buffer.writeIntLE(msgSequence);
			buffer.writeBytes(msg);
			channel.writeAndFlush(buffer);
			return true;
		} else {
			LOGGER.warn("发送消息失败{}，连接异常", msg.getClass().getName());
		}
		return false;
	}

	/**
	 * 获取IP地址
	 *
	 * @param channel
	 * @return
	 */
	public final static String getIp(Channel channel) {
		try {
			if (channel != null && channel.isActive()) {
				InetSocketAddress clientIP = (InetSocketAddress) channel.remoteAddress();
				return clientIP.getAddress().getHostAddress();
			}
		} catch (Exception e) {
		}
		return "0.0.0.0";
	}

	/**
	 * 获取IP：端口
	 *
	 * @param channel
	 * @return
	 */
	public final static String getRemoteIpPort(Channel channel) {
		try {
			if (channel != null && channel.isActive()) {
				InetSocketAddress clientIP = (InetSocketAddress) channel.remoteAddress();
				return clientIP.getAddress().getHostAddress() + ":" + clientIP.getPort();
			}
		} catch (Exception e) {
		}
		return "0.0.0.0:0000";
	}

	/**
	 * 获取本地ip
	 *
	 * @param channel
	 * @return
	 */
	public final static String getLocalIpPort(Channel channel) {
		try {
			if (channel != null && channel.isActive()) {
				InetSocketAddress clientIP = (InetSocketAddress) channel.localAddress();
				return clientIP.getAddress().getHostAddress() + ":" + clientIP.getPort();
			}
		} catch (Exception e) {
		}
		return "0.0.0.0:0000";
	}

	public final static int getMessageID(final byte[] bytes, final int offset) {
		byte[] data = Arrays.copyOfRange(bytes, offset, offset + MSG_ID_LENGTH);
		int msgID = ByteUtil.getInt(data);
		// System.out.println("MsgId------------------>"+msgID);
		if (msgID < 99999) {
			LOGGER.warn("消息类型异常offset{},id{},bytes{}", offset, msgID, ByteUtil.BytesToStr(bytes));
		}
		return msgID;
	}

	/**
	 * 从bytes中获取long的id
	 *
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public final static long getMessagePID(final byte[] bytes, final int offset) {
		byte[] data = Arrays.copyOfRange(bytes, offset, offset + MSG_PID_LENGTH);
		long pid = ByteUtil.getLong(data);
		return pid;
	}

	/**
	 * channel id
	 *
	 * @param channel
	 * @return
	 */
	public static long getChannelId(Channel channel) {
		return Long.parseLong(channel.id().toString(), 16);
	}

	/**
	 * 消息解码，去掉消息头长度
	 *
	 * @param ctx
	 * @param in
	 * @param out
	 */
	public static void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
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
		out.add(readRetainedSlice);
	}

}
