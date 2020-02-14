package org.mmo.game.server;

import org.mmo.engine.akka.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;

/**
 * 配置
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Configuration
public class ApplicationConfiguration {

//	@Autowired
//	private ApplicationContext applicationContext;
//
//	@Autowired
//	private SpringExtension springExtension;
//
//	@Bean
//	public ActorSystem actorSystem() {
//		ActorSystem actorSystem = ActorSystem.create("mmo-game", akkConfiguration());
//		springExtension.initialize(applicationContext);
//		return actorSystem;
//	}
//
//	@Bean
//	public Config akkConfiguration() {
//		return ConfigFactory.load();
//	}
}
