package org.jzy.game.gate.tcp.player;

import org.mmo.engine.io.handler.Handler;
import org.mmo.engine.io.handler.TcpHandler;
import org.mmo.engine.io.message.MsgUtil;
import org.jzy.game.gate.service.GateManager;
import org.mmo.common.struct.server.GameServerInfo;
import org.jzy.game.gate.struct.User;
import org.mmo.message.MIDMessage;
import org.mmo.message.PlayerInfoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 玩家加载请求
 *
 * @author jzy
 */
@Handler(mid = MIDMessage.MID.PlayerInfoReq_VALUE, msg = PlayerInfoRequest.class)
public class PlayerInfoReqHandler extends TcpHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(PlayerInfoReqHandler.class);

    @Override
    public void run() {
        //TODO 进行游戏服选择连接
        var request = (PlayerInfoRequest) getMessage();
        User user = GateManager.getInstance().getUserService().getUserByUserId(request.getUserId());
        if (user == null) {
            LOGGER.warn("用户：{}未登录", request.getUserId());
            return;
        }
        Map<Integer, GameServerInfo> gameServers = GateManager.getInstance().getGameTcpService().getGameServers();
        if (gameServers.isEmpty()) {
            LOGGER.warn("无可用游戏服");
            return;
        }
        //TODO 暂时默认选择第一个,
        for (GameServerInfo gameServerInfo : gameServers.values()) {
            if (gameServerInfo.getChannel() == null || !gameServerInfo.getChannel().isActive()) {
                LOGGER.warn("游戏服：{}-{}连接不可用", gameServerInfo.getId(), gameServerInfo.getIp());
                return;
            }

            user.setGameChannel(gameServerInfo.getChannel());
            MsgUtil.sendInnerMsg(gameServerInfo.getChannel(), request, user.getUserId(), MIDMessage.MID.PlayerInfoReq_VALUE);
            break;
        }
    }
}
