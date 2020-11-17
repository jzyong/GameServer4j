package org.mmo.manage.service;

import org.mmo.common.config.server.ManageConfig;
import org.mmo.common.constant.GlobalProperties;
import org.mmo.common.constant.ZKNode;
import org.mmo.common.service.ZkClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 入口类
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Service
public class ManageService {
    @Autowired
    private ZkClientService zkClientService;

    @Autowired
    private GlobalProperties globalProperties;

    @Autowired
    private ManageConfig manageConfig;

    @Value("${server.port}")
    private int httpPort=10;

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        //推送本地配置
        ManageConfig mc = new ManageConfig();
        mc.setHttpPort(httpPort);
        mc.setPublicIp(manageConfig.getPublicIp());
        mc.setId(manageConfig.getId());
        zkClientService.pushConfig(ZKNode.ManageConfig.getKey(globalProperties.getProfile(),String.valueOf(manageConfig.getId())), mc);
    }

}
