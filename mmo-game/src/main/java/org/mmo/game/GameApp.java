package org.mmo.game;

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

	public static void main(String[] args) {
		SpringApplication.run(GameApp.class, args);
	}

	public void run(String... args) throws Exception {
	}

}
