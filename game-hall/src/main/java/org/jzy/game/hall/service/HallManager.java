package org.jzy.game.hall.service;

import org.jzy.game.common.config.server.HallConfig;
import org.jzy.game.hall.db.repository.IPlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 获取Service对象，脚本不能通过spring获取service对象
 */
@Service
public class HallManager {
	private static HallManager instance;

	@Autowired
	ExecutorService executorService;


	@Autowired
	private PlayerService playerService;

	@Autowired
	IPlayerRepository playerRepository;

	@Autowired
	private GateInfoService gateInfoService;

	@Autowired
	private HallConfig hallConfig;
	@Autowired
	private HallService hallService;


	@PostConstruct()
	public void Init() {
		instance=this;
	}

	public static HallManager getInstance(){
		return instance;
	}
	
	public ExecutorService getExecutorService() {
		return executorService;
	}


	public PlayerService getPlayerService() {
		return playerService;
	}

	public IPlayerRepository getPlayerRepository() {
		return playerRepository;
	}

	public GateInfoService getGateInfoService() {
		return gateInfoService;
	}

	public HallService getGameService() {
		return hallService;
	}

	public HallConfig getHallConfig() {
		return hallConfig;
	}
}
