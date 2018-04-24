package org.mmo.db.memory.collection;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import akka.actor.AbstractActor;

/**
 * MAP 数据存储
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Component
public class MapDB extends AbstractActor {
	private final Logger LOGGER = LoggerFactory.getLogger(MapDB.class);
	protected final Map<String, Object> map = new HashMap<>();

	@Override
	public Receive createReceive() {
		Receive receive = receiveBuilder().match(SetRequest.class, message -> {
			LOGGER.debug("收到数据：{}", message.toString());
			map.put(message.getKey(), message.getValue());
		}).matchAny(o -> LOGGER.warn("接收到未知消息：{}", o)).build();

		return receive;
	}

}
