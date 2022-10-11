package org.jzy.game.gate.tcp.player;

import com.jzy.javalib.network.io.handler.Handler;
import com.jzy.javalib.network.io.handler.TcpHandler;
import org.jzy.game.proto.MID;
import org.jzy.game.proto.MessageId;
import org.jzy.game.gate.service.GateManager;
import org.jzy.game.gate.struct.User;
import org.jzy.game.proto.PlayerInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 玩家加载成功
 * @author jzy
 */
@Handler(mid = MID.PlayerInfoRes_VALUE,msg = PlayerInfoResponse.class)
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
