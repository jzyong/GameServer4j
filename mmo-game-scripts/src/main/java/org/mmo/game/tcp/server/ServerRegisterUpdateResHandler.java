package org.mmo.game.tcp.server;

import io.netty.util.Attribute;
import org.mmo.engine.io.handler.Handler;
import org.mmo.engine.io.handler.TcpHandler;
import org.mmo.game.server.client.GameToGateClientHandler;
import org.mmo.message.MIDMessage;
import org.mmo.message.ServerRegisterUpdateResponse;

/**
 * 网关注册返回
 *
 * @author jzy
 */
@Handler(mid = MIDMessage.MID.ServerRegisterUpdateRes_VALUE, msg = ServerRegisterUpdateResponse.class)
public class ServerRegisterUpdateResHandler extends TcpHandler {
    @Override
    public void run() {
        var response=(ServerRegisterUpdateResponse)getMessage();
        Attribute<Integer> attr = channel.attr(GameToGateClientHandler.ServerId);
        attr.setIfAbsent(response.getServerInfo().getId());
    }
}
