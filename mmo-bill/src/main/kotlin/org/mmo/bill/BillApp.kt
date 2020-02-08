package org.mmo.bill

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

/**
 * 参考
 * https://spring.io/guides/tutorials/spring-boot-kotlin/
 *
 * 启动
 */
@SpringBootApplication
@ComponentScan("org.mmo")
class BillApp

fun main(args: Array<String>) {
	runApplication<BillApp>(*args)
}
