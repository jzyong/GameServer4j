package org.mmo.gate.tcp.account;

import io.grpc.stub.StreamObserver;
import org.mmo.engine.io.handler.Handler;
import org.mmo.engine.io.handler.TcpHandler;
import org.mmo.gate.service.GateManager;
import org.mmo.message.AccountServiceGrpc;
import org.mmo.message.LoginRequest;
import org.mmo.message.LoginResponse;
import org.mmo.message.MIDMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录
 * <p>登录因游戏类型需要修改</p>
 *
 * @author jzy
 */
@Handler(mid = MIDMessage.MID.LoginReq_VALUE, msg = LoginRequest.class)
public class LoginHandler extends TcpHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(LoginHandler.class);

    @Override
    public void run() {

        var request = (LoginRequest) getMsg();
        LOGGER.debug("登录消息：{}", request.toString());
        AccountServiceGrpc.AccountServiceStub stub = GateManager.getInstance().getGateToLoginRpcService().randomAccountStub();
        if(stub==null){
            LOGGER.warn("登陆服grpc不可用.. {}",request.toString());
            return;
        }

        stub.login(request, new StreamObserver<LoginResponse>() {
            @Override
            public void onNext(LoginResponse value) {
                LOGGER.debug("登录返回：{}", value.toString());
                sendMsg(value);
            }

            @Override
            public void onError(Throwable t) {
                LOGGER.warn("登录", t);
            }

            @Override
            public void onCompleted() {

            }
        });
    }
}
