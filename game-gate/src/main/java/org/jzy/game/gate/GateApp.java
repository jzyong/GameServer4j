package org.jzy.game.gate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 网关启动
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ComponentScan("org.jzy.game")
public class GateApp implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(GateApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
