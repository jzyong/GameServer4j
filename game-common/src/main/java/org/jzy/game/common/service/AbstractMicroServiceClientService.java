package org.jzy.game.common.service;

import com.google.common.collect.Maps;
import com.jzy.javalib.base.util.MathUtil;
import io.grpc.stub.StreamObserver;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.*;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.curator.x.discovery.details.ServiceCacheListener;
import org.apache.curator.x.discovery.strategies.RoundRobinStrategy;
import org.jzy.game.common.config.server.ServiceConfig;
import org.jzy.game.common.struct.service.AbstractMicroserviceInfo;
import org.jzy.game.common.struct.service.IMicroserviceInfo;
import org.jzy.game.proto.CommonRpcServiceGrpc;
import org.jzy.game.proto.HttpRequest;
import org.jzy.game.proto.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * 通用微服务客户端 <br>
 * <li>每一个微服务都产生一个{@link ServiceDiscovery},允许路径无规则</li>
 * <li>和本地进程无关，是额外的都微服务，考虑调用项目外地微服务</li>
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
public abstract class AbstractMicroServiceClientService<T extends IMicroserviceInfo> {
    public static final Logger LOGGER = LoggerFactory.getLogger(AbstractMicroServiceClientService.class);
    protected ServiceCache<ServiceConfig> serviceCache;

    /**
     * 缓存的微服务
     */
    private Map<String, T> microservice = new ConcurrentHashMap<>();

    private ServiceDiscovery<ServiceConfig> serviceDiscovery;
    private Map<String, ServiceProvider<ServiceConfig>> providers = Maps.newHashMap();

    @Autowired
    private ZkClientService zkClientService;

    /**
     * 启动
     */
    @PostConstruct
    public void start() {
        // 注册服务
        if (serviceDiscovery == null) {
            try {
                JsonInstanceSerializer<ServiceConfig> serializer = new JsonInstanceSerializer<ServiceConfig>(
                        ServiceConfig.class);
                serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceConfig.class)
                        .client(zkClientService.getClient()).basePath(getServicePath()).serializer(serializer)
                        .thisInstance(null).build();
                serviceDiscovery.start();
            } catch (Exception e) {
                LOGGER.error("service push", e);
            }
        }
        serviceCache = serviceDiscovery.serviceCacheBuilder().name(getServiceName()).build();
        serviceCache.addListener(new ServiceCacheListener() {
            @Override
            public void cacheChanged() {
                AbstractMicroServiceClientService.this.cacheChanged();
            }

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {

            }
        });
        try {
            serviceCache.start();
            // 启动加载一下
            randomMicroServiceInfo();
        } catch (Exception e) {
            LOGGER.error("启动微服务", e);
        }
    }

    /**
     * 获取微服务路径
     *
     * @return
     */
    public abstract String getServicePath();

    /**
     * 获取微服务名称
     *
     * @return
     */
    public abstract String getServiceName();

    public abstract T buildIMicroserviceInfo(ServiceInstance<ServiceConfig> serviceInstance);

    /**
     * 缓存改变 ，后起不能监听到之前启动的服务
     */
    protected void cacheChanged() {
        List<ServiceInstance<ServiceConfig>> serviceInstances = serviceCache.getInstances();

        Set<String> nowServiceIds = new HashSet<>();
        for (ServiceInstance<ServiceConfig> serviceInstance : serviceInstances) {
            nowServiceIds.add(serviceInstance.getId());
            // 移除不可用连接
            if (!serviceInstance.isEnabled()) {
                var service = microservice.remove(serviceInstance.getId());
                if (service != null) {
                    service.stop();
                }
                continue;
            }
            var service = microservice.get(serviceInstance.getId());
            // 新建连接
            if (service == null) {
                service = buildIMicroserviceInfo(serviceInstance);
                microservice.put(service.getId(), service);
                service.register();
            }
        }
        // 删除关闭连接
        HashSet<String> preIds = new HashSet<>(microservice.keySet());
        preIds.removeAll(nowServiceIds);
        preIds.forEach(id -> {
            var service = microservice.remove(id);
            service.stop();
        });

    }

    public void destroy() {
        providers.forEach((key, value) -> {
            CloseableUtils.closeQuietly(value);
        });

        if (serviceCache != null) {
            CloseableUtils.closeQuietly(serviceCache);
        }
        if (serviceDiscovery != null) {
            CloseableUtils.closeQuietly(serviceDiscovery);
        }
        microservice.forEach((id, server) -> {
            server.stop();
        });
        LOGGER.info("close micro service:{}", microservice.size());
    }

    /**
     * 移除微服务
     *
     * @param serviceId
     */
    public void removeMicroServiceInfo(String serviceId) {
        T microServiceInfo = getMicroServiceInfo(serviceId);
        if (microServiceInfo != null) {
            microServiceInfo.stop();
        }
    }

    /**
     * 获取数据中心
     *
     * @param serviceId
     * @return
     */
    public T getMicroServiceInfo(String serviceId) {
        return microservice.get(serviceId);
    }

    /**
     * 一般用于单点服务器，如果多个取第一个
     *
     * @return
     */
    public T getMicroServiceInfo() {
        if (microservice.isEmpty()) {
            return randomMicroServiceInfo();
        }
        for (var it : microservice.values()) {
            return it;
        }
        return null;
    }

    /**
     * 随机选择微服务
     */
    public T randomMicroServiceInfo() {
        if (microservice.size() < 1) {
            Collection<ServiceInstance<ServiceConfig>> serviceInstances = getServiceInstances(getServiceName());
            if (serviceInstances == null) {
                LOGGER.warn("micro service：{} no instance available", getServiceName());
                return null;
            }
            serviceInstances.forEach(it -> {
                T t = buildIMicroserviceInfo(it);
                microservice.put(t.getId(), t);
                t.register();
            });
        }
        var serverInfo = MathUtil.random(microservice.values());
        return serverInfo;
    }

    /**
     * 获取所有可用服务
     *
     * @return
     */
    private Collection<ServiceInstance<ServiceConfig>> getServiceInstances(String serviceName) {
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
     * 获取服务对象
     *
     * @param serviceName
     * @return
     */
    private ServiceProvider<ServiceConfig> getServiceProvider(String serviceName) {
        try {
            ServiceProvider<ServiceConfig> provider = providers.get(serviceName);
            if (provider == null) {
                provider = serviceDiscovery.serviceProviderBuilder().serviceName(serviceName)
                        .providerStrategy(new RoundRobinStrategy<>()).build();
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
     * 模拟http，异步请求,随机获取一个连接
     */
    public void httpRequestAsync(String path, long id, String jsonParams,
                                 BiConsumer<HttpResponse, T> responseConsumer) {
        T t = randomMicroServiceInfo();
        if (t == null) {
            LOGGER.warn("微服务不可用，路径：{} 请求失败", path);
            return;
        }
        if ((t instanceof AbstractMicroserviceInfo) == false) {
            LOGGER.error("微服务[{}]未实现http模拟", t.getName());
            return;
        }
        AbstractMicroserviceInfo abstractMicroserviceInfo = (AbstractMicroserviceInfo) t;
        CommonRpcServiceGrpc.CommonRpcServiceStub commonRpcServiceStub = abstractMicroserviceInfo
                .getCommonRpcServiceStub();
        if (commonRpcServiceStub == null) {
            LOGGER.error("微服务[{}]未实现http模拟", t.getName());
            return;
        }
        HttpRequest.Builder builder = HttpRequest.newBuilder().setPath(path).setId(id);
        if (jsonParams != null) {
            builder.setJsonParam(jsonParams);
        }

        commonRpcServiceStub.httpPost(builder.build(), new StreamObserver<HttpResponse>() {
            @Override
            public void onNext(HttpResponse value) {
                if (responseConsumer != null) {
                    responseConsumer.accept(value, t);
                }
            }

            @Override
            public void onError(Throwable t) {
                LOGGER.error("", t);
            }

            @Override
            public void onCompleted() {

            }
        });

    }

}
