package org.mmo.cluster.http;


import org.mmo.cluster.service.ClusterManager;
import org.mmo.engine.io.handler.Handler;
import org.mmo.engine.io.handler.HttpHandler;
import org.mmo.engine.io.message.MsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 请求网关列表
 * <br>
 * 优先连接第一个
 */
@Handler(path="/gatelist")
public class GateListHandler extends HttpHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GateListHandler.class);
    @Override
    public void run() {
        // http://127.0.0.1:8200/gatelist
        try {
            String list= ClusterManager.getInstance().getClusterServerService().getGateList();
            LOGGER.debug("{} 获取{}", MsgUtil.getIp(channel),list);
            if(list.getBytes().length>0) {
            	setMsgBytes(list.getBytes());
            }
        } finally {
            response();
        }
    }
}
