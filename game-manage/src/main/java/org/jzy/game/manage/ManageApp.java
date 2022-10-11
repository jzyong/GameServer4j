package org.jzy.game.manage;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * 后台管理启动类
 * <br>
 * 未完，html网页实现可参考：https://www.yiibai.com/spring-boot/ajax-example.html
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@ComponentScan("org.jzy.game")
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class ManageApp {
    public static final Logger LOGGER = LoggerFactory.getLogger(ManageApp.class);

    public static void main(String[] args) {
        SpringApplication.run(ManageApp.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            LOGGER.trace("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                LOGGER.trace(beanName);
            }
        };
    }
}
