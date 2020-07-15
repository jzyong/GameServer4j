package org.mmo.cluster;

import org.mmo.engine.server.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 集群启动类
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ComponentScan("org.mmo")
public class ClusterApp implements CommandLineRunner{
	private static final Logger LOGGER=LoggerFactory.getLogger(ClusterApp.class);
	
	public static void main(String[] args) {

		SpringApplication.run(ClusterApp.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
	
	
}
