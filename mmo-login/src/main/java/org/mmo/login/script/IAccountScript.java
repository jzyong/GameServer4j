package org.mmo.login.script;

import io.grpc.stub.StreamObserver;
import org.mmo.engine.script.IScript;
import org.mmo.message.LoginRequest;
import org.mmo.message.LoginResponse;

/**
 * 登录
 * @author jzy
 */
public interface IAccountScript extends IScript {

    /**
     * 登录|注册
     * @param request
     * @param responseObserver
     */
    void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver);
}
