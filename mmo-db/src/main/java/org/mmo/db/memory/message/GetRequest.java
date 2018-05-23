package org.mmo.db.memory.message;

/**
 * 获取数据
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public class GetRequest implements scala.Serializable {
	private static final long serialVersionUID = 1L;
	private final String key;

	public GetRequest(String key) {
		super();
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}
