package org.mmo.gate.struct;

import io.netty.channel.Channel;

/**
 * 游戏服信息
 * TODO 使用zookeeper
 * @author jzy
 */
public class GameServerInfo {
    private Channel channel;

    // 服务器ID
    private int id;
    // 地址
    private String ip;
    // 当前状态 -1表示维护，1表示流畅，2表示拥挤；3表示爆满
    private int serverState = 0;
    // 版本号
    private String version;
    // 在线人数
    private int online;


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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
}
