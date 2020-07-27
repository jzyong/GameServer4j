package org.mmo.login.service;

import io.grpc.stub.StreamObserver;
import org.mmo.engine.script.ScriptService;
import org.mmo.login.script.IAccountScript;
import org.mmo.message.AccountServiceGrpc;
import org.mmo.message.LoginRequest;
import org.mmo.message.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 账号注册登录
 * @author jzy
 */
@Service
public class AccountService extends AccountServiceGrpc.AccountServiceImplBase {

    @Autowired
    ScriptService scriptService;

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        scriptService.consumerScript("AccountScript",(IAccountScript script)->script.login(request,responseObserver));
    }
}
