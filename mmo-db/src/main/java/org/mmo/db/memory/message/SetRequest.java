package org.mmo.db.memory.message;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

import akka.serialization.Serialization;

/**
 * 数据存储请求
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public class SetRequest implements scala.Serializable{
	private static final long serialVersionUID = 1L;
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
