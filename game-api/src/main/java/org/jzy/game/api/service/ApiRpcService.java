package org.jzy.game.api.service;

import com.jzy.javalib.network.grpc.RpcServerService;
import org.jzy.game.common.config.server.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * rpc
 */
@Service
public class ApiRpcService extends RpcServerService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private BillingService billingService;

    @Autowired
    private ApiConfig apiConfig;

    @PostConstruct
    public void init() {
        //注册
        registerService(accountService);

        start(apiConfig.getRpcPort());
    }
}
