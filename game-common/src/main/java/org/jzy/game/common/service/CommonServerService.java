//package org.jzy.game.common.service;
//
//
//import io.grpc.stub.StreamObserver;
//import org.jzy.game.common.util.RpcHttpHandler;
//import org.mmo.engine.io.handler.RpcHandler;
//import org.mmo.engine.script.ScriptService;
//import org.mmo.message.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//
///**
// * 服务器管理
// *
// * @author JiangZhiYong
// * @mail 359135103@qq.com
// */
//@Service
//public class CommonServerService extends ServerServiceGrpc.ServerServiceImplBase {
//    private static final Logger LOGGER = LoggerFactory.getLogger(CommonServerService.class);
//
//
//    @Autowired
//    private ScriptService scriptService;
//
//
//    @Override
//    public void loadScript(LoadScriptRequest request, StreamObserver<LoadScriptResponse> responseObserver) {
//        LoadScriptResponse.Builder builder = LoadScriptResponse.newBuilder();
//        builder.setInfo("load script success");
//        scriptService.init((str) -> {
//            LOGGER.error("load script error:{}", str);
//            builder.setInfo(String.format("load script error：%s",str));
//        });
//        responseObserver.onNext(builder.build());
//        responseObserver.onCompleted();
//    }
//
//    @Override
//    public void httpPost(HttpRequest request, StreamObserver<HttpResponse> responseObserver) {
//        RpcHandler rpcHandler = scriptService.getRpcHandler(request.getPath());
//        if(rpcHandler==null){
//            LOGGER.warn("路径：{}逻辑未实现",request.getPath());
//            responseObserver.onNext(HttpResponse.newBuilder().setCode(404).build());
//            responseObserver.onCompleted();
//            return;
//        }
//        RpcHttpHandler handler=(RpcHttpHandler)rpcHandler;
//        handler.setId(request.getId());
//        handler.setRequest(request);
//        handler.setResponseObserver(responseObserver);
//        handler.run();
//        handler.sendMsg();
//    }
//}
