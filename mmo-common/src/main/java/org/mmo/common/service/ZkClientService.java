package org.mmo.common.service;

import com.alibaba.fastjson.JSON;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.mmo.common.constant.GlobalProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

}
