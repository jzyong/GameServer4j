package org.jzy.game.manage.service;

import org.apache.curator.x.discovery.ServiceInstance;
import org.jzy.game.common.config.server.ServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 网关信息
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Service
public class GateInfoService {
    public static final Logger LOGGER = LoggerFactory.getLogger(GateInfoService.class);
    /**
     * 轮询索引
     */
    private final AtomicInteger roundRobinIndex = new AtomicInteger(0);

    @Autowired
    private ManageService manageService;

    /**
     * 轮询获取网关<br>
     * or根据网关人数获取？根据IP地址进行分配
     *
     * @return
     */
    public String roundRobinGate() {
        List<ServiceInstance<ServiceConfig>> instances = manageService.getGateServiceCache().getInstances();
        if (instances.isEmpty()) {
            LOGGER.warn("no gate server can use");
            return "";
        }
        int thisIndex = Math.abs(roundRobinIndex.getAndIncrement());
        ServiceInstance<ServiceConfig> serviceInstance = instances.get(thisIndex % instances.size());
        return serviceInstance.buildUriSpec();
    }

}
