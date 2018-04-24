package org.mmo.game;

import org.mmo.common.db.repository.IPlayerRepository;
import org.mmo.common.struct.object.Player;
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

	public static void main(String[] args) {
		SpringApplication.run(GameApp.class, args);
	}

	public void run(String... args) throws Exception {
		playerRepository.deleteAll();
		Player player = new Player();
		player.setId(1);
		player.setName("jzy");
		playerRepository.save(player);

		System.out.println(playerRepository.findById(1L).toString());
		System.err.println(playerRepository.findByName("jzy").toString());

	}

}
