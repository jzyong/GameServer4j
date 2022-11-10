package org.mmo.db.memory.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 内存缓存
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Service
public class MemoryCacheService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MemoryCacheService.class);
	private Map<String, Object> mapData = new HashMap<>();

	/**
	 * 存储map数据
	 * @param key
	 * @param value
	 */
	public void saveMapData(String key,Object value) {
		mapData.put(key, value);
	}
	
	/**
	 * 获取缓存数据
	 * @param key
	 * @return
	 */
	public Object getMapData(String key) {
		return mapData.get(key);
	}
	
	
}
