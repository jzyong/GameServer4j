package org.mmo.manage.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.ServiceCacheListener;
import org.mmo.common.config.server.ManageConfig;
import org.mmo.common.config.server.ServiceConfig;
import org.mmo.common.constant.GlobalProperties;
import org.mmo.common.constant.ServiceName;
import org.mmo.common.constant.ZKNode;
import org.mmo.common.service.ZkClientService;
import org.mmo.engine.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 入口类
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Service
public class ManageService {
    public static final Logger LOGGER = LoggerFactory.getLogger(ManageService.class);
    //网关配置缓存
    private ServiceCache<ServiceConfig> gateServiceCache;
    //本地http服务
    private ServiceInstance<ServiceConfig> serviceInstance;

    @Autowired
    private ZkClientService zkClientService;

    @Autowired
    private GlobalProperties globalProperties;

    @Autowired
    private ManageConfig manageConfig;

    @Value("${server.port}")
    private int httpPort = 10;

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        try {
            initZkService();
        } catch (Exception e) {
            LOGGER.error("manage init", e);
        }
    }

    @PreDestroy
    public void destroy() {
        if (gateServiceCache != null) {
            CloseableUtils.closeQuietly(gateServiceCache);
        }
        zkClientService.unregisterService(serviceInstance);
    }


    private void initZkService() throws Exception {
        //推送本地配置
        ManageConfig mc = new ManageConfig();
        mc.setHttpPort(httpPort);
        mc.setPublicIp(manageConfig.getPublicIp());
        mc.setId(manageConfig.getId());
        zkClientService.pushConfig(ZKNode.ManageConfig.getKey(globalProperties.getProfile(), String.valueOf(manageConfig.getId())), mc);

        //http服务地址
        serviceInstance = ServiceInstance.<ServiceConfig>builder()
                .id(String.valueOf(manageConfig.getId()))
                .registrationTimeUTC(TimeUtil.currentTimeMillis())
                .name(ServiceName.ManageHttp.name())
                .address(manageConfig.getPublicIp())
                .payload(new ServiceConfig())
                .port(httpPort)
                .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                .build();
        zkClientService.starService(ZKNode.ServicePath.getKey(globalProperties.getProfile()), serviceInstance);

        //监听网关服务器
        gateServiceCache = zkClientService.getServiceDiscovery().serviceCacheBuilder().name(ServiceName.GateClientTcp.name()).build();
        gateServiceCache.addListener(new ServiceCacheListener() {
            @Override
            public void cacheChanged() {
                LOGGER.info("gate service change {}", gateServiceCache.getInstances().size());
                gateServiceCache.getInstances().forEach(it -> {
                    LOGGER.info("now gate:{} {}:{}", it.getId(), it.getAddress(), it.getPort());
                });
            }

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {

            }
        });
        gateServiceCache.start();
    }

    public ServiceCache<ServiceConfig> getGateServiceCache() {
        return gateServiceCache;
    }
}
