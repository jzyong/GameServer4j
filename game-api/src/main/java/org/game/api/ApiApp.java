package org.game.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * 处理登录，支付等接口,无状态服务器
 */
@SpringBootApplication()
@ComponentScan("org.game")
@EnableMongoRepositories
public class ApiApp implements CommandLineRunner {
    public static void main(String[] args) {

        SpringApplication.run(ApiApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
