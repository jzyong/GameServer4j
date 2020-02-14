package org.mmo.game;

import org.mmo.common.db.repository.IPlayerRepository;
import org.mmo.common.struct.object.Player;
import org.mmo.db.memory.service.CacheClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * 游戏服启动类
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@SpringBootApplication
@ComponentScan("org.mmo")
@EnableMongoRepositories()
public class GameApp implements CommandLineRunner {

	// TODO 暂时测试数据
	@Autowired
	private IPlayerRepository playerRepository;
	@Autowired
	private CacheClientService cacheClientService;
	

	public static void main(String[] args) {
		SpringApplication.run(GameApp.class, args);
	}

	public void run(String... args) throws Exception {
		playerRepository.deleteAll();
		Player player = new Player();
		player.setId(1);
		player.setName("jzy");
		playerRepository.save(player);

		// System.out.println(playerRepository.findById(1L).toString());
		// System.err.println(playerRepository.findByName("jzy").toString());


//		 try {
//	            ActorRef workerActor = actorSystem.actorOf(springExtension.props("workerActor"), "worker-actor");
//
//	            workerActor.tell(new WorkerActor.Request(), null);
//	            workerActor.tell(new WorkerActor.Request(), null);
//	            workerActor.tell(new WorkerActor.Request(), null);
//
//	            FiniteDuration duration = FiniteDuration.create(1, TimeUnit.SECONDS);
//	            Future<Object> awaitable = Patterns.ask(workerActor, new WorkerActor.Response(), Timeout.durationToTimeout(duration));
//
//	            logger.info("Response: " + Await.result(awaitable, duration));
//	        } finally {
//	            actorSystem.terminate();
//	            Await.result(actorSystem.whenTerminated(), Duration.Inf());
//	        }
//		
		cacheClientService.saveMapData("jzy", 30);
//		CompletionStage<Object> mapData = cacheClientService.getMapData("jzy");
		Integer result = (Integer) cacheClientService.getMapData("jzy");
		System.out.println(result);
		Thread.sleep(10000);
	}

}
