package org.jzy.game.manage.controller;

import org.jzy.game.manage.service.RpcClientService;
import org.jzy.game.manage.service.ServerGMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器GM操作
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@RestController()
public class ServerGMController {
    public static final Logger LOGGER = LoggerFactory.getLogger(ServerGMController.class);
    @Autowired
    private ServerGMService serverGMService;
    @Autowired
    private RpcClientService rpcClientService;

    /**
     * 加载脚本
     * <p>
     * http://127.0.0.1:7020/server/gm/load/scripts?serverType=4&&serverId=1
     * http://127.0.0.1:7020/server/gm/load/scripts?serverType=3&&serverId=1
     * </p>
     *
     * @param serverType
     * @param serverId
     * @param path
     * @return
     */
    @RequestMapping("/server/gm/load/scripts")
    public ResponseEntity loadScript(int serverType, int serverId, String path) {
        //TODO
        // return serverGMService.loadScript(serverType, serverId, path);
        return null;
    }

    /**
     * 关闭服务器
     * <p>
     * http://127.0.0.1:7020/server/gm/close?serverType=3&&serverId=1
     * </p>
     *
     * @param serverType
     * @param serverId
     * @return
     */
    @RequestMapping("/server/gm/close")
    public ResponseEntity closeServer(int serverType, int serverId) {
//        ServerServiceGrpc.ServerServiceBlockingStub serverServiceBlocking = rpcClientService.getServerServiceBlocking(ServerType.valueOf(serverType), serverId);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("test", "hello");
//        jsonObject.put("id", 1);
//        var httpResponse = serverServiceBlocking.httpPost(HttpRequest.newBuilder().setPath("/server/gm/close").setJsonParam(jsonObject.toJSONString()).build());
//        LOGGER.info("关服：{}", httpResponse.toString());
//        return new ResponseEntity(httpResponse.getResult(), HttpStatus.OK);
        //TODO
        return null;
    }

}
