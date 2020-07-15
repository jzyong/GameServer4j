package org.mmo.cluster.server.tcp;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.mmo.engine.io.handler.TcpHandler;
import org.mmo.engine.io.message.MsgType;
import org.mmo.engine.io.message.MsgUtil;
import org.mmo.engine.io.message.TcpMessageBean;
import org.mmo.engine.io.service.TcpService;
import org.mmo.engine.script.ScriptService;
import org.mmo.engine.thread.IExecutorService;
import org.mmo.message.MIDMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * 登录服tcp消息处理
 * 
 * @author JiangZhiYong
 * @date 2018/12/11
 */
@Deprecated
public class ClusterTcpServerHandler extends ChannelInboundHandlerAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClusterTcpServerHandler.class);

	private ScriptService scriptService;

	private IExecutorService executorService;
	
	private TcpService tcpService;

	public ClusterTcpServerHandler(ScriptService scriptService,IExecutorService executorService, TcpService tcpService) {
		this.executorService=executorService;
		this.scriptService=scriptService;
		this.tcpService=tcpService;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.warn("已打开连接{}", ctx.channel().toString());
		tcpService.onChannelConnect(ctx.channel());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		sessionClosed(ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		LOGGER.warn("连接{} {}【异常关闭】", ctx.channel().remoteAddress(), cause.getMessage());
		sessionClosed(ctx.channel());
	}

	public void sessionClosed(Channel channel) {
		LOGGER.warn("连接{}已关闭", channel);
		tcpService.onChannelClosed(channel);
		LOGGER.warn(MsgUtil.getLocalIpPort(channel) + "断开链接");
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
				TcpMessageBean messageBean = scriptService.getMessagebean(msgId);
				if (messageBean != null) {
					Message message = messageBean.buildMessage(bytes);
					TcpHandler handler = (TcpHandler) messageBean.newHandler();
					if (handler != null) {
						handler.setPid(id);
						handler.setMsgBytes(bytes);
						handler.setMessage(message);
						handler.setChannel(ctx.channel());
						Executor executor = executorService.getExecutor(messageBean.getExecuteThread());
						executor.execute(handler);
					}
				} else {
					LOGGER.warn("消息[{}]代码未实现逻辑", MIDMessage.MID.forNumber(msgId));
				}
			} else {
				LOGGER.warn("消息类型{}未实现,玩家{}消息发送失败", msgType, id);
			}
		} catch (Exception e) {
			LOGGER.error(String.format("channelRead"), e);
		} finally {
			byteBuf.release();
		}
	}

}
