package org.mmo.db.memory.server;

import org.mmo.db.memory.actor.MapCacheActor;
import org.mmo.db.memory.message.CacheType;
import org.mmo.engine.akka.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import scala.concurrent.duration.FiniteDuration;

/**
 * 服务器缓存应用
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@SpringBootApplication
@ComponentScan("org.mmo")
public class ServerCacheApp implements CommandLineRunner{
	@Autowired
	private ActorSystem actorSystem;

	@Autowired
	private SpringExtension springExtension;

	public static void main(String[] args) {
		 SpringApplication.run(ServerCacheApp.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		 try {
	            ActorRef workerActor = actorSystem.actorOf(springExtension.props("mapCacheActor"), CacheType.Map.getName());

//	            FiniteDuration duration = FiniteDuration.create(1, TimeUnit.SECONDS);
//	            Future<Object> awaitable = Patterns.ask(workerActor, new WorkerActor.Response(), Timeout.durationToTimeout(duration));
//
//	            logger.info("Response: " + Await.result(awaitable, duration));
	        } finally {
//	            actorSystem.terminate();
//	            Await.result(actorSystem.whenTerminated(), Duration.Inf());
	        }
	}
}
