package org.mmo.game.service;

import com.google.protobuf.Message;
import org.apache.curator.x.discovery.ServiceInstance;
import org.mmo.common.config.server.ServiceConfig;
import org.mmo.game.struct.GateServerInfo;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 网关服信息
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Service
public class GateInfoService {

    /**
     * 网关服务器 key serverId
     */
    private Map<String, GateServerInfo> gateServers = new ConcurrentHashMap<>();


    /**
     * 更新网关列表
     *
     * @param serviceInstances
     */
    public void updateGateServer(List<ServiceInstance<ServiceConfig>> serviceInstances) {
        Set<String> nowServiceIds = new HashSet<>();
        for (ServiceInstance<ServiceConfig> serviceInstance : serviceInstances) {
            nowServiceIds.add(serviceInstance.getId());
            //移除不可用连接
            if (!serviceInstance.isEnabled()) {
                GateServerInfo gateServerInfo = gateServers.remove(serviceInstance.getId());
                if (gateServerInfo != null) {
                    gateServerInfo.stop();
                }
                continue;
            }
            GateServerInfo gateServerInfo = gateServers.get(serviceInstance.getId());
            //新建连接
            if (gateServerInfo == null) {
                gateServerInfo = new GateServerInfo(serviceInstance.getId(), serviceInstance.getAddress(), serviceInstance.getPort());
                gateServers.put(gateServerInfo.getId(), gateServerInfo);
                gateServerInfo.connectGate();
            }
        }
        //删除关闭连接
        HashSet<String> preIds = new HashSet<>(gateServers.keySet());
        preIds.removeAll(nowServiceIds);
        preIds.forEach(id -> {
            GateServerInfo gateServerInfo = gateServers.remove(id);
            gateServerInfo.stop();
        });
    }

    /**
     * 删除网关连接
     *
     * @param serverId
     */
    public GateServerInfo deleteGateServer(String serverId) {
        GateServerInfo gateServerInfo = gateServers.remove(serverId);
        if (gateServerInfo != null) {
            gateServerInfo.stop();
        }
        return gateServerInfo;
    }

    /**
     * 广播消息
     * @param message
     * @param messageId
     * @param playerId
     */
    public void broadcastMessage(Message message, int messageId, long playerId) {
        gateServers.forEach((id, server) -> {
            server.sendMsg(message, messageId, playerId);
        });
    }

}
