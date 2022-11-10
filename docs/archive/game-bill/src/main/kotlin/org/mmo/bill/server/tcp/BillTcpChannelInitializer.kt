package org.mmo.bill.server.tcp

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.timeout.IdleStateHandler
import org.mmo.bill.service.BillExecutorService
import org.mmo.engine.io.netty.config.NettyProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * channel初始化
 */
@Component
@Scope("prototype")
class BillTcpChannelInitializer :ChannelInitializer<SocketChannel>(){

    @Autowired
    private var nettyProperties:NettyProperties?=null

    @Autowired
    private var tcpService:BillTcpService?=null

    @Autowired
    private var billExecutorService:BillExecutorService?=null

    override fun initChannel(ch: SocketChannel) {
        ch.pipeline().addLast("Codec",BillTcpByteToMessageCodec())
        ch.pipeline().addLast("MessageHandler",BillTcpServerHandler(billExecutorService,tcpService))

        val nettyServerConfig = nettyProperties!!.serverConfigs[0]
        val bothIdleTime = Math.min(nettyServerConfig.readerIdleTime, nettyServerConfig.writerIdleTime)
        ch.pipeline().addLast("IdleStateHandler", IdleStateHandler(nettyServerConfig.readerIdleTime,
                nettyServerConfig.writerIdleTime, bothIdleTime))
    }
}