package org.mmo.engine.io.grpc;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * rpc 客户端
 */
public abstract class RpcClientService {
    private static final Logger LOGGER= LoggerFactory.getLogger(RpcClientService.class);

    private ManagedChannel channel;

    @Autowired
    private RpcProperties rpcProperties;


    public void start(){
       channel=  ManagedChannelBuilder.forTarget(rpcProperties.getServerURL()).usePlaintext().build();
    }

    public void stop(){
        channel.shutdownNow();
    }

    public ManagedChannel getChannel() {
        return channel;
    }
}
