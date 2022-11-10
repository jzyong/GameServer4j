package org.mmo.cluster.service;

import org.mmo.engine.io.grpc.RpcServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * rpc
 */
@Service
public class ClusterRpcService extends RpcServerService {

    @Autowired
    private ClusterServerService clusterServerService;

    @PostConstruct
    public void init(){

        registerService(clusterServerService);

        start();
    }
}
