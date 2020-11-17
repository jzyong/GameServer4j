package org.mmo.engine.io.grpc;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * rpc
 */
public class RpcServerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerService.class);

    protected Server server;

    @Autowired
    protected RpcProperties rpcProperties;

    private List<BindableService> services = new ArrayList<>();


    /**
     * 注册service
     *
     * @param service
     */
    public void registerService(BindableService service) {
        services.add(service);
    }


    /**
     * 启动rpc
     */
    @Deprecated
    public void start() {
        try {
            ServerBuilder serverBuilder = ServerBuilder.forPort(rpcProperties.getServerPort());
            services.forEach(service -> serverBuilder.addService(service));
            server = serverBuilder.build().start();
            LOGGER.info("rpc started,listening on {}", rpcProperties.getServerPort());
        } catch (Exception e) {
            LOGGER.error("rpc star error", e);
        }
    }

    public void start(int rpcPort) {
        try {
            ServerBuilder serverBuilder = ServerBuilder.forPort(rpcPort);
            services.forEach(service -> serverBuilder.addService(service));
            server = serverBuilder.build().start();
            LOGGER.info("rpc started,listening on {}", rpcPort);
        } catch (Exception e) {
            LOGGER.error("rpc star error", e);
        }
    }

    public void stop() {
        server.shutdownNow();
    }

}
