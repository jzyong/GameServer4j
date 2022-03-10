package org.mmo.game.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.details.ServiceCacheListener;
import org.mmo.common.config.server.GameConfig;
import org.mmo.common.config.server.ServiceConfig;
import org.mmo.common.constant.GlobalProperties;
import org.mmo.common.constant.ServiceName;
import org.mmo.common.constant.ThreadType;
import org.mmo.common.constant.ZKNode;
import org.mmo.common.scripts.IServerScript;
import org.mmo.common.service.CommonServerService;
import org.mmo.common.service.RpcService;
import org.mmo.common.service.ZkClientService;
import org.mmo.engine.script.ScriptService;
import org.mmo.engine.thread.Scene.AbstractScene;
import org.mmo.engine.util.IdUtil;
import org.mmo.engine.util.TimeUtil;
import org.mmo.game.db.MongoGameService;
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
public class GameService extends AbstractScene {
    public static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);

    /**
     * 网关连接缓存
     */
    private ServiceCache<ServiceConfig> gateServiceCache;

    @Autowired
    private ScriptService scriptService;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private GameConfig gameConfig;
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
            IdUtil.SERVER_ID = gameConfig.getId();
            LOGGER.info("game：{}-{} start...", gameConfig.getId(), gameConfig.getPrivateIp());
            initZkService();

            scriptService.init((str) -> {
                LOGGER.error("脚本加载错误:{}", str);
                System.exit(0);
            });
            initRpcService();
//            //初始化数据库
//            mongoGameService.init();

            executorService.registerScene(ThreadType.server.toString(), this);
            scheduleAtFixedRate(() -> {
                scriptService.consumerScript("GameServerScript", (IServerScript script) -> script.updateServerInfo());
            }, 3, 3, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOGGER.error("game start", e);
        }
    }

    private void initRpcService() {
        rpcService.registerService(commonServerService);
        rpcService.start(gameConfig.getRpcPort());
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
        zkClientService.pushConfig(ZKNode.GameConfig.getKey(globalProperties.getProfile(), gameConfig.getId()), gameConfig);
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
