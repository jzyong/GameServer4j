package org.mmo.log;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 *日志
 * @author jiangzhiyong
 */
@SpringBootApplication
@ComponentScan("org.mmo")
public class LogApp implements CommandLineRunner {




    public static void main( String[] args ) {
        SpringApplication.run(LogApp.class,args);
    }


    @Override
    public void run(String... args) throws Exception {

    }
}
