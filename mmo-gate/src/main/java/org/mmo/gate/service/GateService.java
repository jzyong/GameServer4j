package org.mmo.gate.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.ServiceCacheListener;
import org.mmo.common.config.server.GateConfig;
import org.mmo.common.config.server.ServiceConfig;
import org.mmo.common.constant.ServiceName;
import org.mmo.common.constant.ThreadType;
import org.mmo.common.constant.ZKNode;
import org.mmo.common.scripts.IServerScript;
import org.mmo.common.service.ZkClientService;
import org.mmo.engine.script.ScriptService;
import org.mmo.engine.server.ServerProperties;
import org.mmo.engine.thread.Scene.AbstractScene;
import org.mmo.engine.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 网关服务器管理
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Service
public class GateService extends AbstractScene {
    private static final Logger LOGGER = LoggerFactory.getLogger(GateService.class);

    private ServiceInstance<ServiceConfig> clientServiceInstance;
    private ServiceInstance<ServiceConfig> gameServiceInstance;
    ServiceCache<ServiceConfig> loginServiceCache;

    @Autowired
    private GateExecutorService executorService;

    @Autowired
    private ScriptService scriptService;
    @Autowired
    private GateConfig gateConfig;
    @Autowired
    private ZkClientService zkClientService;
    @Autowired
    private GateToLoginRpcService gateToLoginRpcService;

    @Value("${global.profile}")
    private String profile;


    @PostConstruct
    public void init() {
        try {
            LOGGER.info("gate service start：{}-{}...", gateConfig.toString());
            scriptService.init((str) -> {
                LOGGER.error("脚本加载错误：{}", str);
                System.exit(0);
            });

            initZkService();
            executorService.registerScene(ThreadType.server.toString(), this);
        } catch (Exception e) {
            LOGGER.error("init gate server");
        }

    }

    private void initZkService() throws Exception {
        long now = TimeUtil.currentTimeMillis();
        //推送配置
        zkClientService.pushConfig(ZKNode.GateConfig.getKey(profile, gateConfig.getId()), gateConfig);
        //游戏接口服务
        gameServiceInstance = ServiceInstance.<ServiceConfig>builder()
                .id(String.valueOf(gateConfig.getId()))
                .registrationTimeUTC(now)
                .name(ServiceName.GateGameTcp.name())
                .address(gateConfig.getPrivateIp())
                .payload(new ServiceConfig())
                .port(gateConfig.getGameTcpPort())
                .uriSpec(new UriSpec("{address}:{port}"))
                .build();
        zkClientService.starService(ZKNode.ServicePath.getKey(profile), gameServiceInstance);
        //客户端服务
        clientServiceInstance = ServiceInstance.<ServiceConfig>builder()
                .id(String.valueOf(gateConfig.getId()))
                .registrationTimeUTC(now)
                .name(ServiceName.GateClientTcp.name())
                .address(gateConfig.getPublicIp())
                .payload(new ServiceConfig())
                .port(gateConfig.getClientTcpPort())
                .uriSpec(new UriSpec("{address}:{port}"))
                .build();
        zkClientService.registerService(clientServiceInstance);
        //监听登陆服
        loginServiceCache = zkClientService.getServiceDiscovery().serviceCacheBuilder().name(ServiceName.LoginRpc.name()).build();
        loginServiceCache.addListener(new ServiceCacheListener() {
            @Override
            public void cacheChanged() {
                LOGGER.info("login service change {}", loginServiceCache.getInstances().size());
                gateToLoginRpcService.updateLoginRpc(loginServiceCache.getInstances());
            }

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {

            }
        });
        loginServiceCache.start();
    }


    @PreDestroy
    public void destroy() {
        zkClientService.unregisterService(clientServiceInstance);
        zkClientService.unregisterService(gameServiceInstance);
        LOGGER.info("gate service close");
    }

}
