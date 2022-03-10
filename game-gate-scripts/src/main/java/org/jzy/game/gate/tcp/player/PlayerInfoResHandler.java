package org.jzy.game.gate.tcp.player;

import org.mmo.engine.io.handler.Handler;
import org.mmo.engine.io.handler.TcpHandler;
import org.jzy.game.gate.service.GateManager;
import org.jzy.game.gate.struct.User;
import org.mmo.message.MIDMessage;
import org.mmo.message.PlayerInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 玩家加载成功
 * @author jzy
 */
@Handler(mid = MIDMessage.MID.PlayerInfoRes_VALUE,msg = PlayerInfoResponse.class)
public class PlayerInfoResHandler extends TcpHandler {
    public static final Logger LOGGER= LoggerFactory.getLogger(PlayerInfoResHandler.class);
    @Override
    public void run() {
        var response=(PlayerInfoResponse)getMessage();
        User user = GateManager.getInstance().getUserService().getUserByUserId(response.getUserId());
        if(user==null){
            LOGGER.warn("用户：{}未登录",response.getUserId());
            return;
        }
        user.setPlayerId(response.getPlayer().getPlayerId());
        GateManager.getInstance().getUserService().onPlayerLoadSuccess(user);
        user.sendToUser(response);
    }
}
