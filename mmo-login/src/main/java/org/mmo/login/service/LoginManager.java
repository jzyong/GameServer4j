package org.mmo.login.service;

import org.mmo.engine.io.grpc.RpcProperties;
import org.mmo.engine.server.ServerProperties;
import org.mmo.login.db.repository.AccountRepository;
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
	LoginServerService loginServerService;

	@Autowired
	BillingService billingService;

	@Autowired
	AccountService accountService;

	@Autowired
	ServerProperties serverProperties;

	@Autowired
	RpcProperties rpcProperties;

	@Autowired
	LoginToClusterRpcService loginToClusterRpcService;

	@Autowired
	IAccountRepository accountRepository;
	
	@PostConstruct()
	public void Init() {
		instance=this;
	}
	
	public static LoginManager getInstance() {
		return instance;
	}

	public LoginServerService getLoginServerService() {
		return loginServerService;
	}

	public BillingService getBillingService() {
		return billingService;
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public ServerProperties getServerProperties() {
		return serverProperties;
	}

	public LoginToClusterRpcService getLoginToClusterRpcService() {
		return loginToClusterRpcService;
	}

	public RpcProperties getRpcProperties() {
		return rpcProperties;
	}

	public IAccountRepository getAccountRepository() {
		return accountRepository;
	}
}
