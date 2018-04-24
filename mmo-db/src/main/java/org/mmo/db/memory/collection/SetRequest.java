package org.mmo.db.memory.collection;

import com.alibaba.fastjson.JSON;

/**
 * 数据存储请求
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public class SetRequest {
	private final String key;
	private final Object value;

	public SetRequest(String key, Object value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public String toString() {
		return JSON.toJSONString(this);
		
	}
}
