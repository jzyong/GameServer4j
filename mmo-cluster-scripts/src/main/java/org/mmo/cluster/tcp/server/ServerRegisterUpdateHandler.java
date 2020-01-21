package org.mmo.cluster.tcp.server;

import com.proto.MIDMessage.MID;
import com.proto.ServerMessage;


import org.mmo.cluster.service.ClusterManager;
import org.mmo.cluster.service.ClusterServerService;
import org.mmo.common.constant.ServerType;
import org.mmo.engine.io.handler.Handler;
import org.mmo.engine.io.handler.TcpHandler;
import org.mmo.engine.io.message.MsgUtil;
import org.mmo.engine.server.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器内部请求注册更新服务器信息
 */
@Handler(mid = MID.ServerRegisterUpdateReq_VALUE, msg = ServerMessage.ServerRegisterUpdateRequest.class)
public final class ServerRegisterUpdateHandler extends TcpHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRegisterUpdateHandler.class);

    @Override
    public void run() {
        ServerMessage.ServerRegisterUpdateRequest reqMessage = getMsg();
        ServerMessage.ServerInfo serverInfo= reqMessage.getServerInfo();
       LOGGER.debug("{}",serverInfo.toString());

        String ip = serverInfo.getIp()!=null && serverInfo.getIp().length()>0 ?serverInfo.getIp() : MsgUtil.getIp(channel);
        String wwwip = serverInfo.getWwwip();
        int serverId = serverInfo.getId();
        ServerType serverType = ServerType.valueof(serverInfo.getType());
        ClusterServerService clusterServerService = ClusterManager.getInstance().getClusterServerService();
        
        
        ServerInfo info = clusterServerService.getServerInfo(serverType, serverId);
        if (info == null) {//非游戏服务器注册信息为空
            info = new ServerInfo();
            info.setId(serverId);
            clusterServerService.addServerInfo(serverType, info);
        }
        info.setWwwip(wwwip);
        info.setIp(ip);
        info.setId(serverId);
        info.setPort(serverInfo.getPort());
        if (serverType == ServerType.GATE) {
            info.setOnline(serverInfo.getOnline());
            info.setServerState(serverInfo.getState());
            info.setName(serverInfo.getName());
            clusterServerService.updateGateServer();
        }
    }
}
