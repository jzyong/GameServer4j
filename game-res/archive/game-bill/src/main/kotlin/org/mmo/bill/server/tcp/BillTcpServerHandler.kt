package org.mmo.bill.server.tcp

import io.netty.buffer.ByteBuf
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.mmo.engine.io.handler.TcpHandler
import org.mmo.engine.io.message.MsgType
import org.mmo.engine.io.message.MsgUtil
import org.mmo.engine.thread.IExecutorService
import org.mmo.message.MIDMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 登录服tcp消息处理
 *
 * @author JiangZhiYong
 * @date 2018/12/11
 */
class BillTcpServerHandler(private val executorService: IExecutorService?, private val tcpService: BillTcpService?) : ChannelInboundHandlerAdapter() {
   private val LOGGER:Logger=LoggerFactory.getLogger(BillTcpServerHandler::class.java)

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        LOGGER.warn("已打开连接{}", ctx.channel().toString())
        tcpService?.onChannelConnect(ctx.channel())
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        sessionClosed(ctx.channel())
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        LOGGER.warn("连接{} {}【异常关闭】", ctx.channel().remoteAddress(), cause.message)
        sessionClosed(ctx.channel())
    }

    fun sessionClosed(channel: Channel?) {
        LOGGER.warn("连接{}已关闭", channel)
        tcpService?.onChannelClosed(channel)
        LOGGER.warn(MsgUtil.getLocalIpPort(channel) + "断开链接")
    }

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val byteBuf = msg as ByteBuf
        try {
            val msgType = byteBuf.readShort().toInt()
            val id = byteBuf.readLong()
            if (msgType == MsgType.IDMESSAGE.type.toInt()) { // 数据结构:msgId:pfbytes
                val msgId = byteBuf.readInt()
                val bytes = ByteArray(byteBuf.readableBytes())
                byteBuf.readBytes(bytes)
                // 在本地注册，必须预处理
                val messageBean = tcpService?.getMessageBean(msgId)
                if (messageBean != null) {
                    val message = messageBean.buildMessage(bytes)
                    val handler = messageBean.newHandler() as TcpHandler
                    if (handler != null) {
                        handler.pid = id
                        handler.msgBytes = bytes
                        handler.setMessage(message)
                        handler.channel = ctx.channel()
                        val executor = executorService?.getExecutor(messageBean.executeThread)
                        executor?.execute(handler)
                    }
                } else {
                    LOGGER.warn("消息[{}]代码未实现逻辑", MIDMessage.MID.forNumber(msgId))
                }
            } else {
                LOGGER.warn("消息类型{}未实现,玩家{}消息发送失败", msgType, id)
            }
        } catch (e: Exception) {
            LOGGER.error(String.format("channelRead"), e)
        } finally {
            byteBuf.release()
        }
    }


}