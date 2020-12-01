package org.mmo.manage.service;

import org.mmo.common.constant.ServerType;
import org.mmo.common.struct.server.GameServerInfo;
import org.mmo.common.struct.server.LoginServerInfo;
import org.mmo.message.LoadScriptRequest;
import org.mmo.message.LoadScriptResponse;
import org.mmo.message.ServerRegisterUpdateRequest;
import org.mmo.message.ServerServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务器gm操作
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Service
public class ServerGMService {

    @Autowired
    private RpcClientService rpcClientService;

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
        ServerServiceGrpc.ServerServiceBlockingStub serverServiceBlocking = rpcClientService.getServerServiceBlocking(serverType2, serverId);
        if (serverServiceBlocking == null) {
            return new ResponseEntity(String.format("server id %d not exist", serverId), HttpStatus.NOT_FOUND);
        }
        var request = ServerRegisterUpdateRequest.newBuilder();
        LoadScriptRequest.Builder builder = LoadScriptRequest.newBuilder();
        if (path != null) {
            builder.setPath(path);
        }
        LoadScriptResponse loadScriptResponse = serverServiceBlocking.loadScript(builder.build());
        String info = loadScriptResponse.getInfo();
        return new ResponseEntity(info, HttpStatus.OK);
    }
}
