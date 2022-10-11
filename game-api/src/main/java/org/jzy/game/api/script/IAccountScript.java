package org.jzy.game.api.script;

import com.jzy.javalib.base.script.IScript;
import io.grpc.stub.StreamObserver;
import org.jzy.game.proto.LoginRequest;
import org.jzy.game.proto.LoginResponse;

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
