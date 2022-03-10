package org.mmo.bill.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
class BillingController {
    val counter=AtomicLong()

    @GetMapping("/billing")
    fun getBilling(@RequestParam(value = "info" ,defaultValue = "账单") info: String):Billing{
        return Billing(counter.incrementAndGet(),"您的账单信息：$info")
    }
}