package org.mmo.manage.controller;

import org.mmo.manage.service.ServerGMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器GM操作
 * @author jzy
 * @mail 359135103@qq.com
 */
@RestController()
public class ServerGMController {
    @Autowired
    private ServerGMService serverGMService;

    /**
     * 加载脚本
     * <p>
     *     http://127.0.0.1:7020/server/gm/load/scripts?serverType=4&&serverId=1
     *     http://127.0.0.1:7020/server/gm/load/scripts?serverType=3&&serverId=1
     * </p>
     *
     * @param serverType
     * @param serverId
     * @param path
     * @return
     */
    @RequestMapping("/server/gm/load/scripts")
    public ResponseEntity loadScript(int serverType, int serverId, String path){
        return serverGMService.loadScript(serverType,serverId,path);
    }

}
