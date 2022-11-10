package org.mmo.bill.server.tcp

import org.mmo.engine.io.message.TcpMessageBean
import org.mmo.engine.io.service.TcpService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.PostConstruct


/**
 * tcp通信
 */
@Service
class BillTcpService :TcpService(){
    val LOGGER :Logger=LoggerFactory.getLogger(BillTcpService::class.java)


    // 存TCP消息处理类
    var messagebeans: Map<Int, TcpMessageBean<*, *>> = ConcurrentHashMap()


    @Autowired
    private var nettyServer:TcpService?=null

//    @Autowired
//    private var billTcpChannelInitializer:


    @PostConstruct
    fun initHandler(){
        //TODO 注册消息实体
    }


    /**
     * 获取tcp消息结构
     */
    fun  getMessageBean(id:Int):TcpMessageBean<*,*>?{
        return messagebeans.get(id)
    }

}