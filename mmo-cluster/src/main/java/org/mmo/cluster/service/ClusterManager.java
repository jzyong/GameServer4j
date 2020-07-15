package org.mmo.cluster.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 获取Service对象，脚本不能通过spring获取service对象
 */
@Service
public class ClusterManager {
	private static ClusterManager instance;
	
	@Autowired
	ClusterServerService clusterServerService;
	
	@PostConstruct()
	public void Init() {
		instance=this;
	}
	
	public static ClusterManager getInstance() {
		return instance;
	}
	
	public ClusterServerService getClusterServerService() {
		return clusterServerService;
	}
}
