package org.mmo.login.service;

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

    @PostConstruct
    public void init(){
        //注册
       //registerService(clusterServerService);

        start();
    }
}
