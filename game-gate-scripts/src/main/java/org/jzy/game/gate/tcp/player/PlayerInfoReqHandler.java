package org.jzy.game.gate.tcp.player;

import com.jzy.javalib.network.io.handler.Handler;
import com.jzy.javalib.network.io.handler.TcpHandler;
import com.jzy.javalib.network.io.message.MsgUtil;
import org.jzy.game.common.struct.server.GameServerInfo;
import org.jzy.game.proto.MID;
import org.jzy.game.proto.MessageId;
import org.jzy.game.gate.service.GateManager;
import org.jzy.game.gate.struct.User;
import org.jzy.game.proto.PlayerInfoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 玩家加载请求
 *
 * @author jzy
 */
@Handler(mid = MID.PlayerInfoReq_VALUE, msg = PlayerInfoRequest.class)
public class PlayerInfoReqHandler extends TcpHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(PlayerInfoReqHandler.class);

    @Override
    public void run() {
        //TODO 进行游戏服选择连接
        var request = (PlayerInfoRequest) getRequest();
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
            MsgUtil.sendInnerMsg(gameServerInfo.getChannel(), request, user.getUserId(), MID.PlayerInfoReq_VALUE);
            break;
        }
    }
}
