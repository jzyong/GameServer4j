package org.mmo.bill

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * 参考
 * https://spring.io/guides/tutorials/spring-boot-kotlin/
 *
 * 启动
 */
@SpringBootApplication
class BillApp

fun main(args: Array<String>) {
	runApplication<BillApp>(*args)
}
