package org.jzy.game.common.service;


import com.jzy.javalib.network.io.handler.HandlerManager;
import com.jzy.javalib.network.io.handler.RpcHandler;
import com.jzy.javalib.network.io.message.MsgUtil;
import io.grpc.stub.StreamObserver;
import org.jzy.game.common.util.RpcHttpHandler;
import org.jzy.game.proto.CommonRpcServiceGrpc;
import org.jzy.game.proto.HttpRequest;
import org.jzy.game.proto.HttpResponse;
import org.jzy.game.proto.MID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


/**
 * 服务器管理
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Service
public class CommonServerService extends CommonRpcServiceGrpc.CommonRpcServiceImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonServerService.class);


    @PostConstruct
    public void init() {
        //初始化返回消息id和消息的对应关系
        Map<String, Integer> messageNameId = new HashMap<>();
        for (var mid : MID.values()) {
            if (mid.name().contains("Res")) {
                messageNameId.put(mid.name() + "ponse", mid.getNumber());
            }
        }
        MsgUtil.MessageNameIds = messageNameId;
        MsgUtil.MessageIdRule = MsgUtil.MessageIdProtoName;
    }


    @Override
    public void httpPost(HttpRequest request, StreamObserver<HttpResponse> responseObserver) {
        RpcHandler rpcHandler = HandlerManager.getInstance().getRpcHandler(request.getPath());
        if (rpcHandler == null) {
            LOGGER.warn("路径：{}逻辑未实现", request.getPath());
            responseObserver.onNext(HttpResponse.newBuilder().setCode(404).build());
            responseObserver.onCompleted();
            return;
        }
        RpcHttpHandler handler = (RpcHttpHandler) rpcHandler;
        handler.setId(request.getId());
        handler.setRequest(request);
        handler.setResponseObserver(responseObserver);
        handler.run();
        handler.sendMsg();
    }
}
