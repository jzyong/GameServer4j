package org.mmo.game.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis管理
 * TODO
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Component
public class RedisService {
	protected StringRedisTemplate template;

	@Autowired
	public RedisService(StringRedisTemplate template) {
		this.template = template;
	}

}
