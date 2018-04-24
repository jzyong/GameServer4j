package org.mmo.db.memory.collection;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;

/**
 * Map存储缓存
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public class MapDBTest {
	ActorSystem actorSystem=ActorSystem.create();
	
	@Test
	public void saveDataTest() {
		TestActorRef<MapDB> actorRef=TestActorRef.create(actorSystem, Props.create(MapDB.class));
		actorRef.tell(new SetRequest("jzy", "hello") , ActorRef.noSender());
		MapDB mapDB = actorRef.underlyingActor();
		assertEquals(mapDB.map.get("jzy"), "hello");
	}
}
