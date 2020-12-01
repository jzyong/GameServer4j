package org.mmo.common.service;


import io.grpc.stub.StreamObserver;
import org.mmo.engine.script.ScriptService;
import org.mmo.message.LoadScriptRequest;
import org.mmo.message.LoadScriptResponse;
import org.mmo.message.ServerServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 服务器管理
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Service
public class CommonServerService extends ServerServiceGrpc.ServerServiceImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonServerService.class);


    @Autowired
    private ScriptService scriptService;


    @Override
    public void loadScript(LoadScriptRequest request, StreamObserver<LoadScriptResponse> responseObserver) {
        LoadScriptResponse.Builder builder = LoadScriptResponse.newBuilder();
        builder.setInfo("load script success");
        scriptService.init((str) -> {
            LOGGER.error("load script error:{}", str);
            builder.setInfo(String.format("load script error：%s",str));
        });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
