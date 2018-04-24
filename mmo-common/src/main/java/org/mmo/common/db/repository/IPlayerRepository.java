package org.mmo.common.db.repository;

import org.mmo.common.struct.object.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * 玩家数据操作
 * <br>不需要自己实现逻辑，spring 处理
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Component
public interface IPlayerRepository extends MongoRepository<Player, Long>{

	/**
	 * 通过名字查找
	 * @param name
	 * @return
	 */
	public Player findByName(String name);
}
