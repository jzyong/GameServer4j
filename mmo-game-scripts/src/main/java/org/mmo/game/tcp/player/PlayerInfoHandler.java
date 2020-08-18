package org.mmo.game.tcp.player;


import org.mmo.engine.io.handler.Handler;
import org.mmo.engine.io.handler.TcpHandler;
import org.mmo.engine.util.IdUtil;
import org.mmo.engine.util.math.MathUtil;
import org.mmo.game.db.struct.Player;
import org.mmo.game.service.GameManager;
import org.mmo.message.MIDMessage;
import org.mmo.message.PlayerInfo;
import org.mmo.message.PlayerInfoRequest;
import org.mmo.message.PlayerInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 玩家加载请求
 *
 * @author jzy
 */
@Handler(mid = MIDMessage.MID.PlayerInfoReq_VALUE, msg = PlayerInfoRequest.class)
public class PlayerInfoHandler extends TcpHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(PlayerInfoHandler.class);

    @Override
    public void run() {
        var request = (PlayerInfoRequest) getMessage();
        List<Player> players = GameManager.getInstance().getPlayerRepository().findByUserId(request.getUserId());
        if (players.isEmpty()) {
            Player player = new Player();
            player.setId(IdUtil.getId());
            player.setLevel(1);
            player.setUserId(request.getUserId());
            player.setName("Test" + MathUtil.random(10000000));
            GameManager.getInstance().getPlayerRepository().save(player);
            players.add(player);
        }
        Player player = players.get(0);
        player.setChannel(channel);
        GameManager.getInstance().getPlayerService().getOnlinePlayers().put(player.getId(), player);
        PlayerInfoResponse.Builder builder = PlayerInfoResponse.newBuilder();
        PlayerInfo.Builder playerInfo = PlayerInfo.newBuilder();
        playerInfo.setPlayerId(player.getId());
        playerInfo.setExp(player.getExp());
        playerInfo.setLevel(player.getLevel());
        playerInfo.setName(player.getName());
        builder.setPlayer(playerInfo);
        builder.setUserId(request.getUserId());
        player.sendMsg(builder.build(), MIDMessage.MID.PlayerInfoRes_VALUE);
        LOGGER.info("玩家信息：{}", builder.build().toString());
    }
}
