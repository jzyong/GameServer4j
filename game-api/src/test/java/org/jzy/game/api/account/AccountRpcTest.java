package org.jzy.game.api.account;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.Before;
import org.junit.Test;
import org.jzy.game.proto.AccountServiceGrpc;
import org.jzy.game.proto.LoginRequest;
import org.jzy.game.proto.LoginResponse;

import java.text.MessageFormat;

/**
 * 账号 rpc
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
public class AccountRpcTest {

    ManagedChannel channel;
    AccountServiceGrpc.AccountServiceBlockingStub accountServiceBlockingStub;

    @Before
    public void init() {
        channel = ManagedChannelBuilder.forTarget("127.0.0.1:7000").usePlaintext().build();
        accountServiceBlockingStub = AccountServiceGrpc.newBlockingStub(channel);
    }

    /**
     * 登录
     */
    @Test
    public void testLogin() {
        LoginRequest.Builder builder = LoginRequest.newBuilder();
        builder.setAccount("jzy");
        builder.setPassword("111111");
        LoginResponse loginResponse = accountServiceBlockingStub.login(builder.build());
        System.out.println(MessageFormat.format("登录返回：{0}", loginResponse));
    }

}
