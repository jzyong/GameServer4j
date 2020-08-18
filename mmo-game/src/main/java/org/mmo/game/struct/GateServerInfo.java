package org.mmo.game.struct;

import com.google.protobuf.Message;
import org.mmo.engine.io.message.MsgUtil;
import org.mmo.engine.io.netty.config.NettyClientConfig;
import org.mmo.engine.io.netty.tcp.TcpClient;
import org.mmo.engine.server.ServerInfo;
import org.mmo.game.server.client.GameToGateChannelInitializer;
import org.mmo.message.MIDMessage;

/**
 * 连接的网关服信息
 *
 * @author jzy
 */
public class GateServerInfo {
    private ServerInfo serverInfo;

    private TcpClient gateTcpClient;


    /**
     * 连接网关
     */
    public void connectGate() {
        gateTcpClient = new TcpClient();
        NettyClientConfig nettyClientConfig = new NettyClientConfig();
        nettyClientConfig.setIp(serverInfo.getIp());
        nettyClientConfig.setPort(serverInfo.getGateGamePort());
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


    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public TcpClient getGateTcpClient() {
        return gateTcpClient;
    }

    public void setGateTcpClient(TcpClient gateTcpClient) {
        this.gateTcpClient = gateTcpClient;
    }
}
