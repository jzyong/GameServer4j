package org.mmo.cluster.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

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
