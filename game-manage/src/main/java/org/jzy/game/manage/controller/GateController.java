package org.jzy.game.manage.controller;

import org.jzy.game.manage.service.GateInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网关
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@RestController
public class GateController {

    @Autowired
    private GateInfoService gateInfoService;


    /**
     * 获取可连接的网关地址
     * <br>轮询获取，
     * http://127.0.0.1:7061/gate/get
     * http://127.0.0.1:7062/gate/get
     * @return
     */
    @RequestMapping("/gate/get")
    public String getConnectGate() {
        return gateInfoService.roundRobinGate();
    }
}
