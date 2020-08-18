package org.mmo.gate.struct;

import io.netty.channel.Channel;
import org.mmo.engine.server.ServerInfo;

/**
 * 游戏服信息
 * @author jzy
 */
public class GameServerInfo {
    private ServerInfo serverInfo;
    private Channel channel;

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
