package org.mmo.db.memory.service;

import static akka.pattern.Patterns.ask;
import static scala.compat.java8.FutureConverters.toJava;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.annotation.PostConstruct;

import org.mmo.db.memory.message.CacheType;
import org.mmo.db.memory.message.GetRequest;
import org.mmo.db.memory.message.SetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

/**
 * 缓存客户端
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Component()
public class CacheClientService {
	private static final Logger LOGGER=LoggerFactory.getLogger(CacheClientService.class);
	@Autowired
	private ActorSystem system;
	private ActorSelection mapCacheDB;

	@Value("${cache.db.url}")
	private String dbUrl;

	@PostConstruct
	private void init() {
		mapCacheDB = system.actorSelection(dbUrl + CacheType.Map.getName());
	}

	public CacheClientService() {
	
	}

	/**
	 * 存储map数据
	 * @param key
	 * @param value
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public CompletionStage saveMapData(String key, Object value) {
		return toJava(ask(mapCacheDB, new SetRequest(key, value), 2000));
	}

	/**
	 * 获取map数据
	 * @param key
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object getMapData(String key) {
		CompletionStage<Object> completionStage = toJava(ask(mapCacheDB, new GetRequest(key), 2000));
		if(completionStage==null) {
			return null;
		}
		
		try {
			return ((CompletableFuture)completionStage).get();
		} catch (Exception e) {
			LOGGER.error("获取map数据", e);
		}
		return null;
	}
}
