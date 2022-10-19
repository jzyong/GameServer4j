package org.jzy.game.gate.tcp.game;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 游戏服信息(大厅和其他服务器) ,网关使用
 *
 * @author jzy
 */
public class GameServerInfo {
    public static final Logger LOGGER = LoggerFactory.getLogger(GameServerInfo.class);
    private Channel channel;

    private io.grpc.Channel rpcChannel;

    // 服务器ID
    private int id;
    // 地址
    private String ip;
    // 当前状态 -1表示维护，1表示流畅，2表示拥挤；3表示爆满
    private int serverState = 0;
    // 在线人数
    private int online;
    //服务器类型 大厅|子游戏
    private int serverType;
    /**
     * 连接地址
     */
    private String url;

    public GameServerInfo() {
    }

    public GameServerInfo(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getServerState() {
        return serverState;
    }

    public void setServerState(int serverState) {
        this.serverState = serverState;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public io.grpc.Channel getRpcChannel() {
        return rpcChannel;
    }

    public void setRpcChannel(io.grpc.Channel rpcChannel) {
        this.rpcChannel = rpcChannel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }
}
