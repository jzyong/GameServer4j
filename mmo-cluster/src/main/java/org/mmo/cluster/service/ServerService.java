package org.mmo.cluster.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 服务器管理
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Service
public class ServerService {
	private static final Logger LOGGER=LoggerFactory.getLogger(ServerService.class);
	
	/**
	 * 初始化
	 */
	@PostConstruct
	public void init() {
		LOGGER.info("服务器初始化");
	}
	
	
	/**
	 * 销毁
	 */
	@PreDestroy
	public void destory() {
		LOGGER.info("服务器销毁");
	}
	
}
