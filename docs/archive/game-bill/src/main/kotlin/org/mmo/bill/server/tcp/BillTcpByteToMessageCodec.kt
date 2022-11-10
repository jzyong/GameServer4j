package org.mmo.bill.server.tcp

import com.google.protobuf.Message
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageCodec
import org.mmo.engine.io.message.IDMessage
import org.mmo.engine.io.message.MsgType
import org.mmo.engine.io.message.MsgUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 * 解析游戏服连接的消息
 */
class BillTcpByteToMessageCodec : ByteToMessageCodec<Any> (){
    val LOGGER:Logger=LoggerFactory.getLogger(BillTcpByteToMessageCodec::class.java)
    private val HEADER_EXCLUDE_LENGTH = 14

    override fun encode(ctx: ChannelHandlerContext?, msg: Any, out: ByteBuf?) {
        // 需要组装玩家id，消息id等额外信息
        if (msg is IDMessage) {
            val idMessage: IDMessage = msg as IDMessage
            if (idMessage.getMsg() is ByteArray) {
                val bytes = idMessage.getMsg() as ByteArray
                out!!.writeInt(HEADER_EXCLUDE_LENGTH + bytes.size)
                out.writeShort(MsgType.IDMESSAGE.type.toInt())
                out.writeLong(idMessage.getId())
                out.writeInt(idMessage.getMsgId())
                out.writeBytes(bytes)
            } else if (idMessage.msg is Message) {
                val message: Message = idMessage.msg as Message
                val bytes: ByteArray = message.toByteArray()
                out!!.writeInt(HEADER_EXCLUDE_LENGTH + bytes.size)
                out.writeShort(MsgType.IDMESSAGE.type.toInt())
                out.writeLong(idMessage.getId())
                out.writeInt(idMessage.getMsgId())
                out.writeBytes(bytes)
            } else {
                LOGGER.warn("IDMessage加密类型{}未实现", idMessage.getMsg().javaClass.getSimpleName())
            }
        } else {
            LOGGER.warn("加密类型{}未实现", msg::class.java.simpleName)
        }
    }

    override fun decode(ctx: ChannelHandlerContext?, `in`: ByteBuf?, out: MutableList<Any>?) {
        MsgUtil.decode(ctx, `in`, out)
    }
}