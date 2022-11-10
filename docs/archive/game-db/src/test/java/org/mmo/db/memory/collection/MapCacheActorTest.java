package org.mmo.db.memory.collection;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mmo.db.memory.actor.MapCacheActor;
import org.mmo.db.memory.message.SetRequest;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;

/**
 * Map存储缓存
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public class MapCacheActorTest {
	ActorSystem actorSystem=ActorSystem.create();
	
	@Test
	public void saveDataTest() {
//		TestActorRef<MapCacheActor> actorRef=TestActorRef.create(actorSystem, Props.create(MapCacheActor.class));
//		actorRef.tell(new SetRequest("jzy", "hello") , ActorRef.noSender());
//		MapCacheActor mapDB = actorRef.underlyingActor();
//		assertEquals(mapDB.map.get("jzy"), "hello");
	}
}
