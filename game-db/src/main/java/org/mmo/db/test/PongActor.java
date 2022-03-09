package org.mmo.db.test;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Status;

/**
 * 请求返回 测试
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public class PongActor extends AbstractActor {

	@Override
	public Receive createReceive() {
		return receiveBuilder().matchEquals("Ping", s -> sender().tell("Pong", ActorRef.noSender()))
				.matchAny(x -> sender().tell(new Status.Failure(new Exception("未知消息")), self())).build();
	}

}
