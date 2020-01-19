package org.mmo.cluster.service;


import org.mmo.cluster.server.http.ClusterHttpService;
import org.mmo.cluster.server.tcp.ClusterTcpService;
import org.mmo.common.constant.ServerType;
import org.mmo.engine.script.ScriptService;
import org.mmo.engine.server.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 服务器管理
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Service
public class ClusterServerService {
	private static final Logger LOGGER= LoggerFactory.getLogger(ClusterServerService.class);

	@Autowired
	private ClusterHttpService httpService;

	@Autowired
	private ClusterTcpService tcpService;

	@Autowired
	private ScriptService scriptService;
	
	   /**
     * 游戏服务器信息 serverId
     */
    private final Map<ServerType, Map<Integer, ServerInfo>> servers = new ConcurrentHashMap<>();
    /**
     * 网关服务器列表
     */
    private final Vector<ServerInfo> gateServerInfos = new Vector<>();
	
	/**
	 * 初始化
	 */
	@PostConstruct
	public void init() {
		LOGGER.info("-------------服务器初始化 begin-------------");

		scriptService.init((str) -> {
            LOGGER.error("脚本加载错误:{}",str);
			System.exit(0);
		});

	}
	
    /**
     * 获取服务器信息
     *
     * @param serverType
     * @param serverId
     * @return
     */
    public ServerInfo getServerInfo(ServerType serverType, int serverId) {
        Map<Integer, ServerInfo> serverMap = servers.get(serverType);
        if (serverMap != null) {
            return serverMap.get(serverId);
        }
        return null;
    }
    
    /**
     * 更新添加服务器信息
     *
     * @param serverType
     * @param serverInfo
     */
    public void addServerInfo(ServerType serverType, ServerInfo serverInfo) {
        Map<Integer, ServerInfo> serverMap = servers.get(serverType);
        if (serverMap == null) {
            serverMap = new HashMap<>();
            servers.put(serverType, serverMap);
        }
        serverMap.put(serverInfo.getId(), serverInfo);
        if (serverType == ServerType.GATE) {
            if (gateServerInfos.size() > 0) {
                for (int i = gateServerInfos.size() - 1; i >= 0; i--) {
                    if (gateServerInfos.get(i) == null || (gateServerInfos.get(i) != serverInfo
                            && gateServerInfos.get(i).getId() == serverInfo.getId())) {
                        gateServerInfos.remove(i);
                    }
                }
            }
            gateServerInfos.add(serverInfo);
            updateGateServer();
        }
    }
    
    /**
     * 更新gate顺序
     */
    public void updateGateServer() {
        Collections.sort(gateServerInfos, (ServerInfo s0, ServerInfo s1) -> {
            return (s0.getOnline()) - (s1.getOnline());
        });
    }
    
    public String getGateList() {
        if (gateServerInfos.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        gateServerInfos.forEach(s -> {
            if (s.getServerState() >= 0) {
                sb.append(s.getWwwip()).append(";");
                LOGGER.debug("网关{} 人数{}", s.getId(), s.getOnline());
            }
        });
        String url = sb.toString();
        return url.substring(0, url.length() - 1);
    }
	
	
	/**
	 * 销毁
	 */
	@PreDestroy
	public void destroy() {
		LOGGER.info("-------------服务器销毁 begin-------------");

	}
	
}
