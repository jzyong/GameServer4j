package org.jzy.game.hall.tcp.server;

import com.jzy.javalib.network.io.handler.Handler;
import com.jzy.javalib.network.io.handler.TcpHandler;
import com.jzy.javalib.network.netty.tcp.TcpClient;
import io.netty.util.Attribute;
import org.jzy.game.proto.MID;
import org.jzy.game.proto.ServerRegisterUpdateResponse;

/**
 * 网关注册返回
 *
 * @author jzy
 */
@Handler(mid = MID.ServerRegisterUpdateRes_VALUE, msg = ServerRegisterUpdateResponse.class)
public class ServerRegisterUpdateResHandler extends TcpHandler {
    @Override
    public void run() {
        var response = (ServerRegisterUpdateResponse) getMessage();
        Attribute<Object> attr = channel.attr(TcpClient.ChannelParamsKey);
        attr.setIfAbsent(String.valueOf(response.getServerInfo().getId()));
    }
}
