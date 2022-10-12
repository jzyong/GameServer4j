package org.jzy.game.api.account;

import com.jzy.javalib.base.script.IInitScript;
import com.jzy.javalib.base.util.IdUtil;
import com.jzy.javalib.base.util.StringUtil;
import com.jzy.javalib.network.grpc.RpcServerManager;
import io.grpc.stub.StreamObserver;
import org.jzy.game.api.service.ApiManager;
import org.jzy.game.api.struct.Account;
import org.jzy.game.common.struct.log.LoginLog;
import org.jzy.game.proto.AccountServiceGrpc;
import org.jzy.game.proto.LoginRequest;
import org.jzy.game.proto.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 账号rpc请求
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
public class AccountRpcScript extends AccountServiceGrpc.AccountServiceImplBase implements IInitScript {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRpcScript.class);

    @Override
    public void init() {
        RpcServerManager.getInstance().registerMutableService(this);
    }

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        LOGGER.debug("登录消息：{}", request.toString());

        if (StringUtil.isEmpty(request.getAccount())) {
            LOGGER.warn("账号请求为空{}", request.toString());
            return;
        }
        if (StringUtil.isEmpty(request.getPassword())) {
            LOGGER.warn("密码请求为空{}", request.toString());
            return;
        }

        Account account = ApiManager.getInstance().getAccountRepository().findByAccount(request.getAccount());
        if (account == null) {
            account = new Account();
            account.setId(IdUtil.getId());
            account.setAccount(request.getAccount());
            account.setPassword(request.getPassword());
            ApiManager.getInstance().getAccountRepository().save(account);
        } else {
            if (!account.getPassword().equals(request.getPassword())) {
                LOGGER.warn("{} 请求密码错误：{} {}", request.getAccount(), request.getPassword(), account.getPassword());
                return;
            }
        }

        //  ApiManager.getInstance().getKafkaProducerService().sendLog(new LoginLog(IdUtil.getId(), account.getId()));

        LoginResponse.Builder builder = LoginResponse.newBuilder();
        builder.setUserId(account.getId());
        LOGGER.debug("登录返回信息：{}", builder.build().toString());
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
