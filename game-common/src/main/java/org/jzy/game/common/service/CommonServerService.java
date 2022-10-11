package org.jzy.game.common.service;


import com.jzy.javalib.network.io.handler.HandlerManager;
import com.jzy.javalib.network.io.handler.RpcHandler;
import io.grpc.stub.StreamObserver;
import org.jzy.game.common.util.RpcHttpHandler;
import org.jzy.game.proto.CommonRpcServiceGrpc;
import org.jzy.game.proto.HttpRequest;
import org.jzy.game.proto.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * 服务器管理
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Service
public class CommonServerService extends CommonRpcServiceGrpc.CommonRpcServiceImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonServerService.class);




    @Override
    public void httpPost(HttpRequest request, StreamObserver<HttpResponse> responseObserver) {
        RpcHandler rpcHandler = HandlerManager.getInstance().getRpcHandler(request.getPath());
        if(rpcHandler==null){
            LOGGER.warn("路径：{}逻辑未实现",request.getPath());
            responseObserver.onNext(HttpResponse.newBuilder().setCode(404).build());
            responseObserver.onCompleted();
            return;
        }
        RpcHttpHandler handler=(RpcHttpHandler)rpcHandler;
        handler.setId(request.getId());
        handler.setRequest(request);
        handler.setResponseObserver(responseObserver);
        handler.run();
        handler.sendMsg();
    }
}
