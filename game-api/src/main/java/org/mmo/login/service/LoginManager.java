package org.mmo.login.service;

import org.mmo.common.service.KafkaProducerService;
import org.mmo.login.db.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 获取Service对象，脚本不能通过spring获取service对象
 */
@Service
public class LoginManager {
    private static LoginManager instance;

    @Autowired
    LoginService loginService;

    @Autowired
    BillingService billingService;

    @Autowired
    AccountService accountService;

    @Autowired
    IAccountRepository accountRepository;

    @Autowired
    KafkaProducerService kafkaProducerService;

    @PostConstruct()
    public void Init() {
        instance = this;
    }

    public static LoginManager getInstance() {
        return instance;
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public BillingService getBillingService() {
        return billingService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public IAccountRepository getAccountRepository() {
        return accountRepository;
    }

    public KafkaProducerService getKafkaProducerService() {
        return kafkaProducerService;
    }
}
