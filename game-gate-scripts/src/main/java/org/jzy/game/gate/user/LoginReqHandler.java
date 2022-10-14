package org.jzy.game.gate.user;

import com.jzy.javalib.network.io.handler.Handler;
import com.jzy.javalib.network.io.handler.TcpHandler;
import io.grpc.stub.StreamObserver;
import org.jzy.game.proto.*;
import org.jzy.game.gate.tcp.user.UserTcpServerHandler;
import org.jzy.game.gate.service.GateManager;
import org.jzy.game.gate.struct.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录
 * <p>登录因游戏类型需要修改</p>
 *
 * @author jzy
 */
@Handler(mid = MID.LoginReq_VALUE, msg = LoginRequest.class)
public class LoginReqHandler extends TcpHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(LoginReqHandler.class);

    @Override
    public void run() {

        var request = (LoginRequest) getRequest();
        LOGGER.debug("登录消息：{}", request.toString());
        AccountServiceGrpc.AccountServiceStub stub = GateManager.getInstance().getApiClientService().randomAccountServiceStub();
        if (stub == null) {
            LOGGER.warn("登陆服grpc不可用.. {}", request.toString());
            return;
        }

        User user = getChannel().attr(UserTcpServerHandler.USER).get();
        if (user == null) {
            LOGGER.warn("用户{} socket创建异常，无可用用户", request.getAccount());
            return;
        }

        stub.login(request, new StreamObserver<LoginResponse>() {
            @Override
            public void onNext(LoginResponse value) {
                LOGGER.debug("登录返回：{}", value.toString());
                user.setUserId(value.getUserId());
                GateManager.getInstance().getUserService().onUserLoginSuccess(user,null);
                sendClientMsg(value);
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
