package org.jzy.game.api.service;

import com.jzy.javalib.base.script.ScriptManager;
import io.grpc.stub.StreamObserver;
import org.jzy.game.api.script.IAccountScript;
import org.jzy.game.proto.AccountServiceGrpc;
import org.jzy.game.proto.LoginRequest;
import org.jzy.game.proto.LoginResponse;
import org.springframework.stereotype.Service;

/**
 * 账号注册登录
 *
 * @author jzy
 */
@Service
public class AccountService extends AccountServiceGrpc.AccountServiceImplBase {


    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        ScriptManager.getInstance().consumerScript("AccountScript", (IAccountScript script) -> script.login(request, responseObserver));
    }
}
