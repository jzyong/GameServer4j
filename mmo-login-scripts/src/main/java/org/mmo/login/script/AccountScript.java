package org.mmo.login.script;


import io.grpc.stub.StreamObserver;
import org.mmo.message.LoginRequest;
import org.mmo.message.LoginResponse;

/**
 * 登录
 *
 * @author jzy
 */
public class AccountScript implements IAccountScript {
    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        //TODO mongo 用户数据操作
        LoginResponse.Builder builder = LoginResponse.newBuilder();
        builder.setUserId(1);
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
