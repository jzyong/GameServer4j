package org.jzy.game.hall.struct;

import com.google.protobuf.Message;
import com.jzy.javalib.network.io.message.MsgUtil;
import com.jzy.javalib.network.netty.config.NettyClientConfig;
import com.jzy.javalib.network.netty.tcp.TcpClient;
import org.jzy.game.hall.server.client.GameToGateChannelInitializer;

/**
 * 连接的网关服信息
 *
 * @author jzy
 */
public class GateServerInfo {
    private TcpClient gateTcpClient;
    /**
     * 网关id
     */
    private String id;
    private String ip;
    private int port;


    public GateServerInfo() {
    }

    public GateServerInfo(String id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

    /**
     * 连接网关
     */
    public void connectGate() {
        gateTcpClient = new TcpClient();
        NettyClientConfig nettyClientConfig = new NettyClientConfig();
        nettyClientConfig.setIp(ip);
        nettyClientConfig.setPort(port);
        nettyClientConfig.setChannelParam(this.id);
        gateTcpClient.setNettyClientConfig(nettyClientConfig);
        gateTcpClient.setChannelInitializer(new GameToGateChannelInitializer());
        gateTcpClient.init();
        gateTcpClient.start();
    }

    /**
     * 向网关发现消息
     */
    public void sendMsg(Message message, int messageId) {
        MsgUtil.sendInnerMsg(gateTcpClient.getChannel(), message, -1, messageId);
    }

    /**
     * 向网关发现消息
     */
    public void sendMsg(Message message, int messageId, long playerId) {
        MsgUtil.sendInnerMsg(gateTcpClient.getChannel(), message, playerId, messageId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public TcpClient getGateTcpClient() {
        return gateTcpClient;
    }

    public void setGateTcpClient(TcpClient gateTcpClient) {
        this.gateTcpClient = gateTcpClient;
    }

    public void stop(){
        gateTcpClient.stop();
    }
}
