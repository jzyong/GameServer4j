package org.mmo.game.script.server;


import org.apache.curator.x.discovery.ServiceCache;
import org.mmo.common.config.server.GameConfig;
import org.mmo.common.config.server.ServiceConfig;
import org.mmo.common.constant.ServerType;
import org.mmo.common.scripts.IServerScript;
import org.mmo.game.service.GameManager;
import org.mmo.message.MIDMessage;
import org.mmo.message.ServerInfo;
import org.mmo.message.ServerRegisterUpdateRequest;
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
        GameManager.getInstance().getGateInfoService().broadcastMessage(serverRegisterUpdateRequest, MIDMessage.MID.ServerRegisterUpdateReq_VALUE, -1);

        //获取网关列表,刚起服需要主动拉取
        ServiceCache<ServiceConfig> gateServiceCache = GameManager.getInstance().getGameService().getGateServiceCache();
        if (gateServiceCache.getInstances().size() != gateServerCount) {
            gateServerCount = gateServiceCache.getInstances().size();
            GameManager.getInstance().getGateInfoService().updateGateServer(gateServiceCache.getInstances());
        }

    }

    @Override
    public ServerRegisterUpdateRequest buildServerRegisterUpdateRequest() {
        GameConfig gameConfig = GameManager.getInstance().getGameConfig();
        serverInfo.setId(gameConfig.getId());
        serverInfo.setType(ServerType.GAME.ordinal());
        serverInfo.setState(1);
        serverInfo.setVersion("");
        serverInfo.setIp(gameConfig.getPrivateIp());
        serverInfo.setName("");
        serverInfo.setWwwip(gameConfig.getPrivateIp());

        register.setServerInfo(serverInfo.build());
        serverInfo.clear();
        return register.build();
    }
}
