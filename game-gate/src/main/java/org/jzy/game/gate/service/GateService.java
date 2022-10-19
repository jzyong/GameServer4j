package org.jzy.game.gate.service;

import com.jzy.javalib.base.script.ScriptManager;
import com.jzy.javalib.base.util.TimeUtil;
import com.jzy.javalib.network.io.handler.HandlerManager;
import com.jzy.javalib.network.scene.AbstractScene;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.ServiceCacheListener;
import org.jzy.game.common.config.server.GateConfig;
import org.jzy.game.common.config.server.ServiceConfig;
import org.jzy.game.common.constant.ServiceName;
import org.jzy.game.common.constant.ThreadType;
import org.jzy.game.common.constant.ZKNode;
import org.jzy.game.common.service.ZkClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

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
    ServiceCache<ServiceConfig> apiServiceCache;

    @Autowired
    private GateExecutorService executorService;

    @Autowired
    private GateConfig gateConfig;
    @Autowired
    private ZkClientService zkClientService;

    @Value("${global.profile}")
    private String profile;


    @PostConstruct
    public void init() {
        try {
            LOGGER.info("gate service start：{}-{}...", gateConfig.toString());
            ScriptManager.getInstance().setHandlerLoader(HandlerManager.getInstance());
            ScriptManager.getInstance().init((str) -> {
                LOGGER.error("load script error：{}", str);
                System.exit(0);
            });

            initZkService();
            executorService.registerScene(ThreadType.server.toString(), this);
        } catch (Exception e) {
            LOGGER.error("init gate server");
        }

    }

    /**
     * 初始化zookeeper
     *
     * @throws Exception
     */
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
    }


    @PreDestroy
    public void destroy() {
        if (apiServiceCache != null) {
            CloseableUtils.closeQuietly(apiServiceCache);
        }
        zkClientService.unregisterService(clientServiceInstance);
        zkClientService.unregisterService(gameServiceInstance);
        LOGGER.info("gate service close");
    }

}
