package org.mmo.login.script;


import io.grpc.stub.StreamObserver;
import org.mmo.engine.util.IdUtil;
import org.mmo.engine.util.StringUtil;
import org.mmo.login.service.LoginManager;
import org.mmo.login.struct.Account;
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

        if (StringUtil.isEmpty(request.getAccount())) {
            LOGGER.warn("账号请求为空{}", request.toString());
            return;
        }
        if (StringUtil.isEmpty(request.getPassword())) {
            LOGGER.warn("密码请求为空{}", request.toString());
            return;
        }

        Account account = LoginManager.getInstance().getAccountRepository().findByAccount(request.getAccount());
        if (account == null) {
            account = new Account();
            account.setId(IdUtil.getId());
            account.setAccount(request.getAccount());
            account.setPassword(request.getPassword());
            LoginManager.getInstance().getAccountRepository().save(account);
        } else {
            if (!account.getPassword().equals(request.getPassword())) {
                LOGGER.warn("{} 请求密码错误：{} {}", request.getAccount(), request.getPassword(),account.getPassword());
                return;
            }
        }

        LoginResponse.Builder builder = LoginResponse.newBuilder();
        builder.setUserId(account.getId());
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
