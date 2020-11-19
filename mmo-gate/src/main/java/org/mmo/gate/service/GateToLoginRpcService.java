package org.mmo.gate.service;

import org.apache.curator.x.discovery.ServiceInstance;
import org.mmo.common.config.server.ServiceConfig;
import org.mmo.common.constant.ServiceName;
import org.mmo.common.service.ZkClientService;
import org.mmo.engine.util.math.MathUtil;
import org.mmo.gate.struct.LoginServerInfo;
import org.mmo.message.AccountServiceGrpc;
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
    private Map<String, LoginServerInfo> loginServerInfoMap = new ConcurrentHashMap<>();

    @Autowired
    private ZkClientService zkClientService;

    /**
     * 随机选择发送消息
     */
    public AccountServiceGrpc.AccountServiceStub randomAccountStub() {
        if (loginServerInfoMap.size() < 1) {
            Collection<ServiceInstance<ServiceConfig>> serviceInstances = zkClientService.getServiceInstances(ServiceName.LoginRpc.name());
            if (serviceInstances == null) {
                return null;
            }
            serviceInstances.forEach(it -> {
                LoginServerInfo loginServerInfo = new LoginServerInfo(it.getId(), it.buildUriSpec());
                loginServerInfoMap.put(loginServerInfo.getId(), loginServerInfo);
                loginServerInfo.connectLogin();
            });
        }
        LoginServerInfo login = MathUtil.random(loginServerInfoMap.values());
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
                LoginServerInfo loginServerInfo = loginServerInfoMap.remove(serviceInstance.getId());
                if (loginServerInfo != null) {
                    loginServerInfo.stop();
                }
                continue;
            }
            LoginServerInfo loginServerInfo = loginServerInfoMap.get(serviceInstance.getId());
            //新建连接
            if (loginServerInfo == null) {
                loginServerInfo = new LoginServerInfo(serviceInstance.getId(), serviceInstance.buildUriSpec());
                loginServerInfoMap.put(loginServerInfo.getId(), loginServerInfo);
                loginServerInfo.connectLogin();
            }
        }
        //删除关闭连接
        HashSet<String> preIds = new HashSet<>(loginServerInfoMap.keySet());
        preIds.removeAll(nowServiceIds);
        preIds.forEach(id -> {
            LoginServerInfo loginServerInfo = loginServerInfoMap.remove(id);
            loginServerInfo.stop();
        });
    }

}
