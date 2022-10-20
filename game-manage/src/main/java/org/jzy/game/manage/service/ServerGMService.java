package org.jzy.game.manage.service;

import com.alibaba.fastjson.JSONObject;
import org.jzy.game.common.constant.ServerType;
import org.jzy.game.common.struct.service.ApiServiceInfo;
import org.jzy.game.common.struct.service.HallServiceInfo;
import org.jzy.game.manage.service.rpc.ApiClientService;
import org.jzy.game.manage.service.rpc.HallClientService;
import org.jzy.game.proto.CommonRpcServiceGrpc;
import org.jzy.game.proto.HttpRequest;
import org.jzy.game.proto.ServerRegisterUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 服务器gm操作
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Service
public class ServerGMService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerGMService.class);

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private HallClientService hallClientService;



    /**
     * 加载脚本
     *
     * @param serverType
     * @param serverId
     * @param path       脚本路径|类
     * @return
     */
    public ResponseEntity loadScript(int serverType, int serverId, String path) {
        ServerType serverType2 = ServerType.valueOf(serverType);
        if (ServerType.NONE == serverType2) {
            return new ResponseEntity(String.format("server type %d not exist", serverType), HttpStatus.NOT_FOUND);
        }
        CommonRpcServiceGrpc.CommonRpcServiceBlockingStub commonRpcServiceBlockingStub = getCommonRpcServiceBlockingStub(serverType2, serverId);
        if (commonRpcServiceBlockingStub == null) {
            return new ResponseEntity(String.format("server id %d not exist", serverId), HttpStatus.NOT_FOUND);
        }

        var httpResponse = commonRpcServiceBlockingStub.withDeadlineAfter(5, TimeUnit.SECONDS).httpPost(HttpRequest.newBuilder().setPath("/server/reload/script").setJsonParam("").build());

        return new ResponseEntity(httpResponse.getResult(), HttpStatus.OK);
    }

    /**
     * 关闭服务器
     *
     * @param serverType
     * @param serverId
     * @return
     */
    public ResponseEntity closeServer(int serverType, int serverId) {
        ServerType serverType2 = ServerType.valueOf(serverType);
        if (ServerType.NONE == serverType2) {
            return new ResponseEntity(String.format("server type %d not exist", serverType), HttpStatus.NOT_FOUND);
        }
        CommonRpcServiceGrpc.CommonRpcServiceBlockingStub commonRpcServiceBlockingStub = getCommonRpcServiceBlockingStub(serverType2, serverId);
        if (commonRpcServiceBlockingStub == null) {
            return new ResponseEntity(String.format("server id %d not exist", serverId), HttpStatus.NOT_FOUND);
        }
        var httpResponse = commonRpcServiceBlockingStub.withDeadlineAfter(5, TimeUnit.SECONDS).httpPost(HttpRequest.newBuilder().setPath("/server/gm/close").setJsonParam("").build());
        LOGGER.info("关服：{}", httpResponse.toString());
        return new ResponseEntity(httpResponse.getResult(), HttpStatus.OK);
    }


    /**
     * 获取 通用服务接口
     *
     * @param serverType
     * @param serverId
     * @return
     */
    private CommonRpcServiceGrpc.CommonRpcServiceBlockingStub getCommonRpcServiceBlockingStub(ServerType serverType, int serverId) {
        switch (serverType) {
            case Api -> {
                ApiServiceInfo microServiceInfo = apiClientService.getMicroServiceInfo(String.valueOf(serverId));
                if (apiClientService != null) {
                    return microServiceInfo.getCommonRpcServiceBlockingStub();
                }
            }
            case Hall -> {
                HallServiceInfo microServiceInfo = hallClientService.getMicroServiceInfo(String.valueOf(serverId));
                if (microServiceInfo != null) {
                    return microServiceInfo.getCommonRpcServiceBlockingStub();
                }
            }
        }
        LOGGER.warn("server type {} id {} not exist", serverType, serverId);
        return null;
    }

}
