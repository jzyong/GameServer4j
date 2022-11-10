package org.mmo.bill.service

import org.mmo.bill.server.tcp.BillTcpChannelInitializer
import org.mmo.bill.server.tcp.BillTcpService
import org.mmo.engine.io.netty.config.NettyProperties
import org.mmo.engine.io.netty.tcp.TcpServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * 服务器管理
 */
@Service
class BillServiceService {
    val LOGGER:Logger=LoggerFactory.getLogger(BillServiceService::class.java);

    @Autowired
    private val nettyServer: TcpServer? = null

    @Autowired
    private var billTcpService:BillTcpService?=null

    @Autowired
    private val nettyProperties: NettyProperties? = null

    @Autowired
    private var billTcpChannelInitializer:BillTcpChannelInitializer?=null

    @PostConstruct
    fun start(){
        LOGGER.debug("run bill tcp ...")
        val nettyServerConfig = nettyProperties!!.serverConfigs[0]
        nettyServer!!.setNettyServerConfig(nettyServerConfig)
        nettyServer.setChannelInitializer(billTcpChannelInitializer)
        nettyServer.start()
    }


    @PreDestroy
    fun stop() {
        LOGGER.debug(" stop ... ")
        nettyServer!!.stop()
    }

}