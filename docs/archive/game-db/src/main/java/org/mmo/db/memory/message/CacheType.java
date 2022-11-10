package org.mmo.db.memory.message;

/**
 * 数据缓存类型
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public enum CacheType {

	
	Map("map"),;

	private String name;

	private CacheType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
