package org.mmo.gate.struct;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.mmo.engine.server.ServerInfo;
import org.mmo.message.AccountServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录服务器rpc信息
 *
 * @author jzy
 */
public class LoginServerInfo {
    public static final Logger LOGGER = LoggerFactory.getLogger(LoginServerInfo.class);
    private ServerInfo serverInfo;

    private ManagedChannel channel;

    private AccountServiceGrpc.AccountServiceStub accountStub;


    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    /**
     * 连接登陆服
     */
    public void connectLogin() {
        channel = ManagedChannelBuilder.forTarget(serverInfo.getWwwip()).usePlaintext().build();
        accountStub = AccountServiceGrpc.newStub(channel);
        LOGGER.info("连接登陆服：{}", serverInfo.getWwwip());
    }

    public void stop() {
        channel.shutdownNow();
    }

    public ManagedChannel getChannel() {
        return channel;
    }

    public AccountServiceGrpc.AccountServiceStub getAccountStub() {
        return accountStub;
    }
}
