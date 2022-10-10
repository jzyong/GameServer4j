package org.jzy.game.gate.service;

import com.jzy.javalib.base.util.MathUtil;
import org.apache.curator.x.discovery.ServiceInstance;
import org.jzy.game.common.config.server.ServiceConfig;
import org.jzy.game.common.constant.ServiceName;
import org.jzy.game.common.service.ZkClientService;
import org.jzy.game.common.struct.server.ApiServerInfo;
import org.jzy.game.proto.AccountServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连接登录服rpc
 * <br>
 * 有多个rpc服务器
 *
 * @author jzy
 */
@Service
public class GateToLoginRpcService {
    public static final Logger LOGGER = LoggerFactory.getLogger(GateToLoginRpcService.class);
    //登陆服列表 key serverId
    private Map<String, ApiServerInfo> apiServerInfoMap = new ConcurrentHashMap<>();

    @Autowired
    private ZkClientService zkClientService;

    /**
     * 随机选择发送消息
     */
    public AccountServiceGrpc.AccountServiceStub randomAccountStub() {
        if (apiServerInfoMap.size() < 1) {
            Collection<ServiceInstance<ServiceConfig>> serviceInstances = zkClientService.getServiceInstances(ServiceName.LoginRpc.name());
            if (serviceInstances == null) {
                return null;
            }
            serviceInstances.forEach(it -> {
                ApiServerInfo loginServerInfo = new ApiServerInfo(it.getId(), it.buildUriSpec());
                apiServerInfoMap.put(loginServerInfo.getId(), loginServerInfo);
                loginServerInfo.connectLogin();
            });
        }
        ApiServerInfo login = MathUtil.random(apiServerInfoMap.values());
        AccountServiceGrpc.AccountServiceStub accountStub = login.getAccountStub();
        if (accountStub == null) {
            login.connectLogin();
            accountStub = login.getAccountStub();
        }
        return accountStub;
    }


    /**
     * 更新rpc连接
     *
     * @param serviceInstances
     */
    public void updateLoginRpc(List<ServiceInstance<ServiceConfig>> serviceInstances) {
        Set<String> nowServiceIds = new HashSet<>();
        for (ServiceInstance<ServiceConfig> serviceInstance : serviceInstances) {
            nowServiceIds.add(serviceInstance.getId());
            //移除不可用连接
            if (!serviceInstance.isEnabled()) {
                ApiServerInfo loginServerInfo = apiServerInfoMap.remove(serviceInstance.getId());
                if (loginServerInfo != null) {
                    loginServerInfo.stop();
                }
                continue;
            }
            ApiServerInfo loginServerInfo = apiServerInfoMap.get(serviceInstance.getId());
            //新建连接
            if (loginServerInfo == null) {
                loginServerInfo = new ApiServerInfo(serviceInstance.getId(), serviceInstance.buildUriSpec());
                apiServerInfoMap.put(loginServerInfo.getId(), loginServerInfo);
                loginServerInfo.connectLogin();
            }
        }
        //删除关闭连接
        HashSet<String> preIds = new HashSet<>(apiServerInfoMap.keySet());
        preIds.removeAll(nowServiceIds);
        preIds.forEach(id -> {
            ApiServerInfo loginServerInfo = apiServerInfoMap.remove(id);
            loginServerInfo.stop();
        });
    }

}
