package org.mmo.gate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 网关启动
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@SpringBootApplication()
@ComponentScan("org.mmo")
public class GateApp implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(GateApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
