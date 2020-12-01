package org.mmo.gate.tcp.server;

import org.mmo.engine.io.handler.Handler;
import org.mmo.engine.io.handler.TcpHandler;
import org.mmo.gate.service.GateManager;
import org.mmo.common.struct.server.GameServerInfo;
import org.mmo.message.MIDMessage;
import org.mmo.message.ServerInfo;
import org.mmo.message.ServerRegisterUpdateRequest;
import org.mmo.message.ServerRegisterUpdateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 游戏服注册到网关
 *
 * @author jzy
 */
@Handler(mid = MIDMessage.MID.ServerRegisterUpdateReq_VALUE, msg = ServerRegisterUpdateRequest.class)
public class ServerRegisterUpdateReqHandler extends TcpHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(ServerRegisterUpdateReqHandler.class);

    @Override
    public void run() {
        var request = (ServerRegisterUpdateRequest) getMessage();
        ServerInfo serverInfo = request.getServerInfo();
        GameServerInfo gameServerInfo = GateManager.getInstance().getGameTcpService().getGameServers().get(serverInfo.getId());
        if (gameServerInfo == null) {
            //TODO 设置channel属性，channel关闭移除服务器信息
            gameServerInfo = new GameServerInfo();
            gameServerInfo.setChannel(this.channel);
            gameServerInfo.setId(serverInfo.getId());
            GateManager.getInstance().getGameTcpService().getGameServers().put(serverInfo.getId(), gameServerInfo);
            LOGGER.info("服务器：{}-{}-{} 连接网关成功", serverInfo.getId(), serverInfo.getName(), serverInfo.getIp());
        }
        gameServerInfo.setChannel(this.channel);
        gameServerInfo.setId(serverInfo.getId());
        gameServerInfo.setIp(serverInfo.getIp());
        gameServerInfo.setVersion(serverInfo.getVersion());
        gameServerInfo.setServerState(serverInfo.getState());
        gameServerInfo.setOnline(serverInfo.getOnline());
        ServerRegisterUpdateResponse.Builder builder = ServerRegisterUpdateResponse.newBuilder();
        ServerInfo.Builder replayServerInfo = ServerInfo.newBuilder();
        replayServerInfo.setId(GateManager.getInstance().getGateConfig().getId());
        builder.setServerInfo(replayServerInfo);
        sendInnerMsg(MIDMessage.MID.ServerRegisterUpdateRes_VALUE, builder.build());
    }
}
