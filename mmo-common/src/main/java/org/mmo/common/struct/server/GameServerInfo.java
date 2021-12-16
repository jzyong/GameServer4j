package org.mmo.common.struct.server;

import io.grpc.ManagedChannelBuilder;
import io.netty.channel.Channel;
import org.mmo.message.ServerServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 游戏服信息
 *
 * @author jzy
 * @mail 359135103@qq.com
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
    // 版本号
    private String version;
    // 在线人数
    private int online;
    /**
     * 连接地址
     */
    private String url;

    /**
     * 服务器连接
     */
    private ServerServiceGrpc.ServerServiceBlockingStub serverServiceBlockingStub;

    public GameServerInfo() {
    }

    public GameServerInfo(int id) {
        this.id = id;
    }


    /**
     * 连接
     */
    public void connectGame() {
        rpcChannel = ManagedChannelBuilder.forTarget(url).usePlaintext().build();
        serverServiceBlockingStub = ServerServiceGrpc.newBlockingStub(rpcChannel);
        LOGGER.info("connect to game：{} {}", id, url);
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

    public ServerServiceGrpc.ServerServiceBlockingStub getServerServiceBlockingStub() {
        return serverServiceBlockingStub;
    }

    public void setServerServiceBlockingStub(ServerServiceGrpc.ServerServiceBlockingStub serverServiceBlockingStub) {
        this.serverServiceBlockingStub = serverServiceBlockingStub;
    }
}
