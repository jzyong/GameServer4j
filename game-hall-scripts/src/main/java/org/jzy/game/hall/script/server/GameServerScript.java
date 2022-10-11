package org.jzy.game.hall.script.server;


import org.apache.curator.x.discovery.ServiceCache;
import org.jzy.game.common.config.server.ServiceConfig;
import org.jzy.game.common.constant.ServerType;
import org.jzy.game.common.scripts.IServerScript;
import org.jzy.game.hall.service.HallManager;
import org.jzy.game.proto.MID;
import org.jzy.game.proto.MessageId;
import org.jzy.game.proto.ServerInfo;
import org.jzy.game.proto.ServerRegisterUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 服務器脚本
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public class GameServerScript implements IServerScript {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameServerScript.class);
    ServerRegisterUpdateRequest.Builder register = ServerRegisterUpdateRequest.newBuilder();
    ServerInfo.Builder serverInfo = ServerInfo.newBuilder();
    private int gateServerCount = 0;

    /**
     * 每3秒定时调用
     */
    @Override
    public void updateServerInfo() {
        //向网关服注册游戏服
        ServerRegisterUpdateRequest serverRegisterUpdateRequest = buildServerRegisterUpdateRequest();
        HallManager.getInstance().getGateInfoService().broadcastMessage(serverRegisterUpdateRequest, MID.ServerRegisterUpdateReq_VALUE, -1);

        //获取网关列表,刚起服需要主动拉取
        ServiceCache<ServiceConfig> gateServiceCache = HallManager.getInstance().getGameService().getGateServiceCache();
        if (gateServiceCache.getInstances().size() != gateServerCount) {
            gateServerCount = gateServiceCache.getInstances().size();
            HallManager.getInstance().getGateInfoService().updateGateServer(gateServiceCache.getInstances());
        }

    }

    public ServerRegisterUpdateRequest buildServerRegisterUpdateRequest() {
        var hallConfig = HallManager.getInstance().getHallConfig();
        serverInfo.setId(hallConfig.getId());
        serverInfo.setType(ServerType.Hall.ordinal());
        serverInfo.setState(1);
        serverInfo.setVersion("");
        serverInfo.setIp(hallConfig.getPrivateIp());
        serverInfo.setName("");
        serverInfo.setWwwip(hallConfig.getPrivateIp());

        register.setServerInfo(serverInfo.build());
        serverInfo.clear();
        return register.build();
    }
}
