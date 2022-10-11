package org.jzy.game.hall.service;

import com.jzy.javalib.base.script.ScriptManager;
import com.jzy.javalib.base.util.IdUtil;
import com.jzy.javalib.base.util.TimeUtil;
import com.jzy.javalib.network.io.handler.HandlerManager;
import com.jzy.javalib.network.scene.AbstractScene;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.details.ServiceCacheListener;
import org.jzy.game.common.config.server.HallConfig;
import org.jzy.game.common.config.server.ServiceConfig;
import org.jzy.game.common.constant.GlobalProperties;
import org.jzy.game.common.constant.ServiceName;
import org.jzy.game.common.constant.ThreadType;
import org.jzy.game.common.constant.ZKNode;
import org.jzy.game.common.scripts.IServerScript;
import org.jzy.game.common.service.CommonServerService;
import org.jzy.game.common.service.RpcService;
import org.jzy.game.common.service.ZkClientService;
import org.jzy.game.hall.db.MongoGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * 服务器管理入口
 *
 * @author jzy
 */
@Service
public class HallService extends AbstractScene {
    public static final Logger LOGGER = LoggerFactory.getLogger(HallService.class);

    /**
     * 网关连接缓存
     */
    private ServiceCache<ServiceConfig> gateServiceCache;

    @Autowired
    private ExecutorService executorService;
    @Autowired
    private HallConfig hallConfig;
    @Autowired
    private ZkClientService zkClientService;
    @Autowired
    private GlobalProperties globalProperties;
    @Autowired
    private GateInfoService gateInfoService;
    @Autowired
    private MongoGameService mongoGameService;
    @Autowired
    private RpcService rpcService;
    @Autowired
    private CommonServerService commonServerService;


    @PostConstruct
    public void init() {
        try {
            IdUtil.SERVER_ID = hallConfig.getId();
            LOGGER.info("game：{}-{} start...", hallConfig.getId(), hallConfig.getPrivateIp());
            initZkService();

            ScriptManager.getInstance().setHandlerLoader(HandlerManager.getInstance());
            ScriptManager.getInstance().init((str) -> {
                LOGGER.error("脚本加载错误:{}", str);
                System.exit(0);
            });
            initRpcService();
//            //初始化数据库
//            mongoGameService.init();

            executorService.registerScene(ThreadType.server.toString(), this);
            scheduleAtFixedRate(() -> {
                ScriptManager.getInstance().consumerScript("GameServerScript", (IServerScript script) -> script.updateServerInfo());
            }, 3, 3, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOGGER.error("game start", e);
        }
    }

    private void initRpcService() {
        rpcService.registerService(commonServerService);
        rpcService.start(hallConfig.getRpcPort());
    }


    @PreDestroy
    public void destroy() {

    }

    /**
     * 初始化zookeeper
     *
     * @throws Exception
     */
    private void initZkService() throws Exception {
        long now = TimeUtil.currentTimeMillis();
        //推送配置
        zkClientService.pushConfig(ZKNode.GameConfig.getKey(globalProperties.getProfile(), hallConfig.getId()), hallConfig);
        zkClientService.starService(ZKNode.ServicePath.getKey(globalProperties.getProfile()), null);
        gateServiceCache = zkClientService.getServiceDiscovery().serviceCacheBuilder().name(ServiceName.GateGameTcp.name()).build();
        gateServiceCache.addListener(new ServiceCacheListener() {
            @Override
            public void cacheChanged() {
                LOGGER.info("gate service change {}", gateServiceCache.getInstances().size());
                gateServiceCache.getInstances().forEach(it -> {
                    LOGGER.info("now gate:{} {}:{}", it.getId(), it.getAddress(), it.getPort());
                });
                gateInfoService.updateGateServer(gateServiceCache.getInstances());
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
