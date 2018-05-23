package org.mmo.db.memory.actor;

import org.mmo.db.memory.message.GetRequest;
import org.mmo.db.memory.message.KeyNotFoundExecption;
import org.mmo.db.memory.message.SetRequest;
import org.mmo.db.memory.service.MemoryCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.AbstractActor;
import akka.actor.Status;

/**
 * MAP 数据存储
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Component("mapCacheActor")
@Scope("prototype")
public class MapCacheActor extends AbstractActor {
	private final Logger LOGGER = LoggerFactory.getLogger(MapCacheActor.class);
	@Autowired
	private MemoryCacheService memoryCacheService;

	@Override
	public Receive createReceive() {
		Receive receive = receiveBuilder().match(SetRequest.class, message -> {
			LOGGER.debug("存储数据：{}", message.toString());
			memoryCacheService.saveMapData(message.getKey(), message.getValue());
			sender().tell(new Status.Success(message.getKey()), self());
		}).match(GetRequest.class, message -> {
			LOGGER.debug("请求数据：{}", message.toString());
			Object value = memoryCacheService.getMapData(message.getKey());
			Object response = (value != null) ? value : new Status.Failure(new KeyNotFoundExecption(message.getKey()));
			sender().tell(response, self());
		}).matchAny(o -> {
			LOGGER.warn("接收到未知消息：{}", o);
			sender().tell(new Status.Failure(new ClassNotFoundException()), self());
		}).build();

		return receive;
	}

}
