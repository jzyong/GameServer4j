package org.jzy.game.common.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.*;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.curator.x.discovery.strategies.RoundRobinStrategy;
import org.jzy.game.common.config.server.ServiceConfig;
import org.jzy.game.common.constant.GlobalProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * zookeeper
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Service
public class ZkClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkClientService.class);

    private CuratorFramework client;

    private CuratorCache curatorCache;

    //服务
    private ServiceDiscovery<ServiceConfig> serviceDiscovery;
    private Map<String, ServiceProvider<ServiceConfig>> providers = Maps.newHashMap();

    /**
     * zk 配置文件
     */
    private Map<String, Object> zkConfigs = new ConcurrentHashMap<>();

    @Autowired
    private GlobalProperties globalProperties;

    @PostConstruct
    public void init() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
        client = CuratorFrameworkFactory.newClient(globalProperties.getZookeeperUrl(), retryPolicy);
        client.start();
    }

    @PreDestroy
    public void destroy() {
        if (curatorCache != null) {
            CloseableUtils.closeQuietly(curatorCache);
        }
        if (serviceDiscovery != null) {
            CloseableUtils.closeQuietly(serviceDiscovery);
        }
        providers.forEach((key, value) -> {
            CloseableUtils.closeQuietly(value);
        });
        CloseableUtils.closeQuietly(client);
    }

    /**
     * 创建缓存
     *
     * @param path
     */
    public void createCache(String path) {
        if (curatorCache != null) {
            LOGGER.warn("缓存已经创建:{}", path);
            return;
        }
        LOGGER.info("创建zk：{}缓存", path);
        curatorCache = CuratorCache.build(client, path);
        CuratorCacheListener listener = CuratorCacheListener.builder().forCreates(node -> {
            if (node.getData() != null && node.getData().length > 0) {
                String data = new String(node.getData());
                LOGGER.debug("创建节点：{}={}", node.getPath(), data);
                zkConfigs.put(node.getPath(), data);
            }
        }).forChanges((oldNode, newNode) -> {
            if (newNode.getData() != null && newNode.getData().length > 0) {
                String data = new String(newNode.getData());
                LOGGER.debug("节点变更：{}={}", newNode.getPath(), data);
                zkConfigs.put(newNode.getPath(), data);
            }
        }).forDeletes(node -> {
            zkConfigs.remove(node.getPath());
            LOGGER.debug("删除节点:{}", node);
        }).forInitialized(() -> {
            LOGGER.debug("初始化");
        }).build();
        curatorCache.listenable().addListener(listener);
        curatorCache.start();
    }

    /**
     * 直接从zk获取数据
     *
     * @param path
     * @param <T>
     * @return
     */
    public <T> T getConfig(String path, Class<T> clazz) {
        try {
            //先从缓存获取
            Object config = zkConfigs.get(path);
            if (config != null && config.getClass().isAssignableFrom(clazz)) {
                return (T) config;
            }

            byte[] bytes = client.getData().forPath(path);
            if (bytes == null) {
                return null;
            }
            String str = new String(bytes);
            T returnObject = null;
            if (clazz.getSimpleName().equalsIgnoreCase(String.class.getSimpleName())) {
                returnObject = (T) str;
            } else {
                returnObject = JSON.parseObject(str, clazz);
            }
            zkConfigs.put(path, returnObject);
            return returnObject;
        } catch (Exception e) {
            LOGGER.error("加载配置：", e);
        }
        return null;
    }

    /**
     * 推送配置
     *
     * @param path
     * @param config
     * @return
     */
    public boolean pushConfig(String path, Object config) {
        try {
            String jsonString = null;
            if (!(config instanceof String)) {
                jsonString = JSON.toJSONString(config);
            } else {
                jsonString = (String) config;
            }
            LOGGER.info("zk:{}-->{}", path, jsonString);
            client.create().orSetData().creatingParentsIfNeeded().forPath(path, jsonString.getBytes());
        } catch (Exception e) {
            LOGGER.error(String.format("push config:%s-->%s", path, config.toString()), e);
        }
        return true;
    }

    /**
     * 启动服务信息
     *
     * @param path 监听根路径
     * @param serviceInstance 当前服务
     */
    public void starService(String path, ServiceInstance<ServiceConfig> serviceInstance) {
        try {
            JsonInstanceSerializer<ServiceConfig> serializer = new JsonInstanceSerializer<ServiceConfig>(ServiceConfig.class);
            serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceConfig.class)
                    .client(client)
                    .basePath(path)
                    .serializer(serializer)
                    .thisInstance(serviceInstance)
                    .build();

            serviceDiscovery.start();
        } catch (Exception e) {
            LOGGER.error("service push", e);
        }
    }


    /**
     * 注册服务
     * @param serviceInstance
     */
    public void registerService(ServiceInstance<ServiceConfig> serviceInstance){
        try {
            serviceDiscovery.registerService(serviceInstance);
        } catch (Exception e) {
            LOGGER.error("register service",e);
        }
    }

    /**
     * 取消注册服务
     * @param serviceInstance
     */
    public void unregisterService(ServiceInstance<ServiceConfig> serviceInstance){
        try {
            serviceDiscovery.unregisterService(serviceInstance);
        } catch (Exception e) {
            LOGGER.error("unregister service",e);
        }
    }


    /**
     * 获取服务对象
     *
     * @param serviceName
     * @return
     */
    private ServiceProvider<ServiceConfig> getServiceProvider(String serviceName) {
        try {
            ServiceProvider<ServiceConfig> provider = providers.get(serviceName);
            if (provider == null) {
                provider = serviceDiscovery.serviceProviderBuilder().serviceName(serviceName).providerStrategy(new RoundRobinStrategy<>()).build();
                providers.put(serviceName, provider);
                provider.start();
            }
            return provider;
        } catch (Exception e) {
            LOGGER.error("get ServiceProvider", e);
        }
        return null;
    }

    /**
     * 循环获取一个可用服务
     *
     * @param serviceName
     * @return
     */
    public ServiceInstance getServiceInstance(String serviceName) {
        try {
            ServiceInstance<ServiceConfig> instance = getServiceProvider(serviceName).getInstance();
            if (instance == null) {
                LOGGER.warn("server {} can not use", serviceName);
                return null;
            }
            return instance;
        } catch (Exception e) {
            LOGGER.error("get ServiceInstance", e);
        }
        return null;
    }


    /**
     * 获取所有可用服务
     *
     * @return
     */
    public Collection<ServiceInstance<ServiceConfig>> getServiceInstances(String serviceName) {

        try {
            Collection<ServiceInstance<ServiceConfig>> allInstances = getServiceProvider(serviceName).getAllInstances();
            if (allInstances.isEmpty()) {
                LOGGER.warn("service {} not find", serviceName);
                return null;
            }
            return allInstances;
        } catch (Exception e) {
            LOGGER.error("get ServiceInstances", e);
        }
        return null;
    }

    /**
     * 获取所有服务
     *
     * @return
     */
    public List<ServiceInstance<ServiceConfig>> getAllService() {
        List<ServiceInstance<ServiceConfig>> serviceInstances = new ArrayList<>();
        try {
            Collection<String> serviceNames = serviceDiscovery.queryForNames();
            for (String serviceName : serviceNames) {
                Collection<ServiceInstance<ServiceConfig>> instances = serviceDiscovery.queryForInstances(serviceName);
                serviceInstances.addAll(instances);
            }
        } catch (Exception e) {
            LOGGER.error("get all service", e);
        }
        return serviceInstances;
    }

    public ServiceDiscovery<ServiceConfig> getServiceDiscovery() {
        return serviceDiscovery;
    }
}
