package org.jzy.game.gate.player;

import com.jzy.javalib.network.io.handler.Handler;
import com.jzy.javalib.network.io.handler.TcpHandler;
import com.jzy.javalib.network.io.message.MsgUtil;
import org.jzy.game.common.constant.ServerType;
import org.jzy.game.gate.tcp.game.GameServerInfo;
import org.jzy.game.proto.MID;
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
        var request = (PlayerInfoRequest) getRequest();
        User user = GateManager.getInstance().getUserService().getUserByPlayerId(request.getUserId());
        if (user == null) {
            LOGGER.warn("用户：{}未登录", request.getUserId());
            return;
        }
        //TODO 暂时默认选择第一个,
        var gameServer = GateManager.getInstance().getGameTcpService().getGameServerInfo(ServerType.Hall.getType(), 1);
        if (gameServer == null) {
            LOGGER.warn("无可用游戏服");
            return;
        }

        if (gameServer.getChannel() == null || !gameServer.getChannel().isActive()) {
            LOGGER.warn("游戏服：{}-{}连接不可用", gameServer.getId(), gameServer.getIp());
            return;
        }

        user.setGameChannel(gameServer.getChannel());
        MsgUtil.sendInnerMsg(gameServer.getChannel(), request, user.getUserId(), MID.PlayerInfoReq_VALUE);
    }
}
