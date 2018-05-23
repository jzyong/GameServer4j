package org.mmo.db.memory.message;

import java.io.Serializable;

/**
 * 请求key 不存在
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public class KeyNotFoundExecption extends Exception implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String key;
	public KeyNotFoundExecption(String key) {
		super();
		this.key = key;
	}
	public String getKey() {
		return key;
	}
	
	
}
