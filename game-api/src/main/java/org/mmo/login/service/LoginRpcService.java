package org.mmo.login.service;

import org.mmo.common.config.server.LoginConfig;
import org.mmo.common.service.CommonServerService;
import org.mmo.engine.io.grpc.RpcServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * rpc
 */
@Service
public class LoginRpcService extends RpcServerService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private BillingService billingService;
    @Autowired
    private CommonServerService commonServerService;

    @Autowired
    private LoginConfig loginConfig;

    @PostConstruct
    public void init() {
        //注册
        registerService(accountService);
        registerService(commonServerService);

        start(loginConfig.getRpcPort());
    }
}
