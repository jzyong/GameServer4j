package org.mmo.manage;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 后台管理启动类
 * <br>
 * 未完，html网页实现可参考：https://www.yiibai.com/spring-boot/ajax-example.html
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@SpringBootApplication
public class ManageApp {

	public static void main(String[] args) {
		SpringApplication.run(ManageApp.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}
		};
	}
}
