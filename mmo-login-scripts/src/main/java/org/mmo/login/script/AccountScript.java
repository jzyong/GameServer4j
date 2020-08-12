package org.mmo.login.script;


import io.grpc.stub.StreamObserver;
import org.mmo.message.LoginRequest;
import org.mmo.message.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录
 *
 * @author jzy
 */
public class AccountScript implements IAccountScript {
    public static final Logger LOGGER = LoggerFactory.getLogger(AccountScript.class);

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        //TODO mongo 用户数据操作
        LOGGER.debug("登录消息：{}", request.toString());
        LoginResponse.Builder builder = LoginResponse.newBuilder();
        builder.setUserId(1);
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
