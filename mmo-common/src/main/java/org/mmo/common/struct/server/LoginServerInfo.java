package org.mmo.common.struct.server;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.mmo.message.AccountServiceGrpc;
import org.mmo.message.ServerServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录服务器rpc信息
 *
 * @author jzy
 */
public class LoginServerInfo {
    public static final Logger LOGGER = LoggerFactory.getLogger(LoginServerInfo.class);

    private ManagedChannel channel;

    private String id;
    /**
     * 连接地址
     */
    private String url;

    private AccountServiceGrpc.AccountServiceStub accountStub;

    /**
     * 服务器连接
     */
    private ServerServiceGrpc.ServerServiceBlockingStub serverServiceBlockingStub;

    public LoginServerInfo() {
    }

    public LoginServerInfo(String id, String url) {
        this.id = id;
        this.url = url;
    }

    /**
     * 连接登陆服
     */
    public void connectLogin() {
        channel = ManagedChannelBuilder.forTarget(url).usePlaintext().build();
        accountStub = AccountServiceGrpc.newStub(channel);
        serverServiceBlockingStub = ServerServiceGrpc.newBlockingStub(channel);
        LOGGER.info("connect to login：{} {}", id, url);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void stop() {
        LOGGER.info("close to login：{} {}", id, url);
        if(channel!=null){
            channel.shutdownNow();
        }

    }

    public ManagedChannel getChannel() {
        return channel;
    }

    public AccountServiceGrpc.AccountServiceStub getAccountStub() {
        return accountStub;
    }

    public void setChannel(ManagedChannel channel) {
        this.channel = channel;
    }

    public void setAccountStub(AccountServiceGrpc.AccountServiceStub accountStub) {
        this.accountStub = accountStub;
    }

    public ServerServiceGrpc.ServerServiceBlockingStub getServerServiceBlockingStub() {
        return serverServiceBlockingStub;
    }

    public void setServerServiceBlockingStub(ServerServiceGrpc.ServerServiceBlockingStub serverServiceBlockingStub) {
        this.serverServiceBlockingStub = serverServiceBlockingStub;
    }
}
