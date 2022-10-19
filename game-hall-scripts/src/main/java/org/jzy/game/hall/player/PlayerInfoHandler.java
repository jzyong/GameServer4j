package org.jzy.game.hall.player;


import com.jzy.javalib.base.util.IdUtil;
import com.jzy.javalib.base.util.MathUtil;
import com.jzy.javalib.network.io.handler.Handler;
import com.jzy.javalib.network.io.handler.TcpHandler;
import org.jzy.game.hall.db.struct.Player;
import org.jzy.game.hall.service.HallManager;
import org.jzy.game.proto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 玩家加载请求
 *
 * @author jzy
 */
@Handler(mid = MID.PlayerInfoReq_VALUE, msg = PlayerInfoRequest.class)
public class PlayerInfoHandler extends TcpHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(PlayerInfoHandler.class);

    @Override
    public void run() {
        var request = (PlayerInfoRequest) getMessage();
        List<Player> players = HallManager.getInstance().getPlayerRepository().findByUserId(request.getUserId());
        if (players.isEmpty()) {
            Player player = new Player();
            player.setId(request.getUserId()); // 暂时一个用户只能创建一个玩家
            player.setLevel(1);
            player.setUserId(request.getUserId());
            player.setName("Test" + MathUtil.random(10000000));
            HallManager.getInstance().getPlayerRepository().save(player);
            players.add(player);
        }
        Player player = players.get(0);
        player.setChannel(channel);
        HallManager.getInstance().getPlayerService().getOnlinePlayers().put(player.getId(), player);
        PlayerInfoResponse.Builder builder = PlayerInfoResponse.newBuilder();
        PlayerInfo.Builder playerInfo = PlayerInfo.newBuilder();
        playerInfo.setPlayerId(player.getId());
        playerInfo.setExp(player.getExp());
        playerInfo.setLevel(player.getLevel());
        playerInfo.setName(player.getName());
        builder.setPlayer(playerInfo);
        builder.setUserId(request.getUserId());
        sendInnerMsg(builder.build());
        LOGGER.info("玩家信息：{}", builder.build().toString());
    }
}
