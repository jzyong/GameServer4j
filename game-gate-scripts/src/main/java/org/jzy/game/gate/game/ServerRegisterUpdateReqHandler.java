package org.jzy.game.gate.game;

import com.jzy.javalib.network.io.handler.Handler;
import com.jzy.javalib.network.io.handler.TcpHandler;
import io.netty.util.Attribute;
import org.jzy.game.gate.service.GateManager;
import org.jzy.game.gate.tcp.game.GameServerInfo;
import org.jzy.game.gate.tcp.game.GameTcpServerHandler;
import org.jzy.game.proto.MID;
import org.jzy.game.proto.ServerInfo;
import org.jzy.game.proto.ServerRegisterUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 游戏服注册到网关
 *
 * @author jzy
 */
@Handler(mid = MID.ServerRegisterUpdateReq_VALUE, msg = ServerRegisterUpdateRequest.class)
public class ServerRegisterUpdateReqHandler extends TcpHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(ServerRegisterUpdateReqHandler.class);

    @Override
    public void run() {
        var request = (ServerRegisterUpdateRequest) getMessage();
        ServerInfo serverInfo = request.getServerInfo();

        GateManager gateManager = GateManager.getInstance();
        if (gateManager == null || gateManager.getGameTcpService() == null) {
            LOGGER.debug("网关未准备好，注册失败：{}-{}", serverInfo.getType(), serverInfo.getId());
            return;
        }
        GameServerInfo gameServerInfo = gateManager.getGameTcpService().getGameServerInfo(serverInfo.getType(), serverInfo.getId());
        if (gameServerInfo == null) {
            gameServerInfo = new GameServerInfo();
            gameServerInfo.setChannel(this.channel);
            gameServerInfo.setId(serverInfo.getId());
            gameServerInfo.setIp(serverInfo.getIp());
            gameServerInfo.setServerType(serverInfo.getType());
            Attribute<GameServerInfo> attr = channel.attr(GameTcpServerHandler.GameServerInfo);
            attr.set(gameServerInfo);
            GateManager.getInstance().getGameTcpService().addGameServerInfo(gameServerInfo);
            LOGGER.info("服务器：{}-{}-{} 连接网关成功", serverInfo.getId(), serverInfo.getName(), serverInfo.getIp());
        }
        //LOGGER.debug("服务器：{}-{}-{} 更新网关成功", serverInfo.getServerId(), serverInfo.getServerName(), serverInfo.getIp());
        gameServerInfo.setChannel(this.channel);
        gameServerInfo.setId(serverInfo.getId());
        gameServerInfo.setIp(serverInfo.getIp());
        gameServerInfo.setServerState(serverInfo.getState());
        gameServerInfo.setOnline(serverInfo.getOnline());
    }
}
