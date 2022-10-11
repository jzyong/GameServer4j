package org.jzy.game.api.service;

import org.jzy.game.api.db.repository.AccountRepository;
import org.jzy.game.common.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 获取Service对象，脚本不能通过spring获取service对象
 */
@Service
public class ApiManager {
    private static ApiManager instance;

    @Autowired
    ApiService apiService;

    @Autowired
    BillingService billingService;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    KafkaProducerService kafkaProducerService;

    @PostConstruct()
    public void Init() {
        instance = this;
    }

    public static ApiManager getInstance() {
        return instance;
    }

    public ApiService getLoginService() {
        return apiService;
    }

    public BillingService getBillingService() {
        return billingService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public KafkaProducerService getKafkaProducerService() {
        return kafkaProducerService;
    }
}
