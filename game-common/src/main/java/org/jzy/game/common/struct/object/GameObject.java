package org.jzy.game.common.struct.object;

import org.springframework.data.annotation.Id;

/**
 * 游戏对象
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public abstract class GameObject {

	/**唯一id*/
	@Id
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
	
}
