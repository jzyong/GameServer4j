package org.jzy.game.manage.controller;

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

    /**
     * 加载脚本
     * <p>
     * http://127.0.0.1:7061/server/gm/load/scripts?serverType=4&&serverId=1
     * http://127.0.0.1:7061/server/gm/load/scripts?serverType=3&&serverId=1
     * </p>
     *
     * @param serverType
     * @param serverId
     * @param path
     * @return
     */
    @RequestMapping("/server/gm/load/scripts")
    public ResponseEntity loadScript(int serverType, int serverId, String path) {
        return serverGMService.loadScript(serverType, serverId, path);
    }

    /**
     * 关闭服务器
     * <p>
     * http://127.0.0.1:7061/server/gm/close?serverType=3&&serverId=1
     * http://127.0.0.1:7061/server/gm/close?serverType=4&&serverId=1
     * </p>
     *
     * @param serverType
     * @param serverId
     * @return
     */
    @RequestMapping("/server/gm/close")
    public ResponseEntity closeServer(int serverType, int serverId) {
        return serverGMService.closeServer(serverType, serverId);
    }

}
