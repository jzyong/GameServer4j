//package org.mmo.login.service;
//
//import org.mmo.common.constant.ServerType;
//import org.mmo.engine.io.grpc.RpcClientService;
//import org.mmo.engine.server.ServerProperties;
//import org.mmo.message.ServerInfo;
//import org.mmo.message.ServerRegisterUpdateRequest;
//import org.mmo.message.ServerServiceGrpc;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//
///**
// * 连接cluster
// * <br>使用zookeeper替代
// */
//@Deprecated
//@Service
//public class LoginToClusterRpcService extends RpcClientService {
//    private static final Logger LOGGER= LoggerFactory.getLogger(LoginToClusterRpcService.class);
//
//    private ServerServiceGrpc.ServerServiceBlockingStub blockingStub;
//    private ServerServiceGrpc.ServerServiceStub stub;
//
//    @Autowired
//    private ServerProperties serverProperties;
//
//   // @PostConstruct
//    public void init(){
//
//        start();
//        blockingStub=ServerServiceGrpc.newBlockingStub(getChannel());
//        stub=ServerServiceGrpc.newStub(getChannel());
//
//
//        var serverInfo= ServerInfo.newBuilder()
//                .setId(serverProperties.getId())
//                .setType(ServerType.LOGIN.ordinal())
//                .setState(1)
//                .setVersion(String.valueOf(serverProperties.getVersion()))
//                .build();
//        var response= blockingStub.serverRegister(ServerRegisterUpdateRequest.newBuilder().setServerInfo(serverInfo).build());
//        LOGGER.info("login 成功注册到 cluster {}",response.toString());
//    }
//
//    public ServerServiceGrpc.ServerServiceBlockingStub getBlockingStub() {
//        return blockingStub;
//    }
//
//    public ServerServiceGrpc.ServerServiceStub getStub() {
//        return stub;
//    }
//}
