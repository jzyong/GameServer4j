package org.jzy.game.hall.db.repository;

import org.jzy.game.hall.db.struct.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

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

	List<Player> findByUserId(long userId);
}
