package org.mmo.game.service;

import org.mmo.common.config.server.GameConfig;
import org.mmo.engine.script.ScriptService;
import org.mmo.game.db.repository.IPlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 获取Service对象，脚本不能通过spring获取service对象
 */
@Service
public class GameManager {
	private static GameManager instance;

	@Autowired
	ExecutorService executorService;

	@Autowired
	private ScriptService scriptService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	IPlayerRepository playerRepository;

	@Autowired
	private GateInfoService gateInfoService;

	@Autowired
	private GameConfig gameConfig;
	@Autowired
	private GameService gameService;


	@PostConstruct()
	public void Init() {
		instance=this;
	}

	public static GameManager getInstance(){
		return instance;
	}
	
	public ExecutorService getExecutorService() {
		return executorService;
	}

	public ScriptService getScriptService() {
		return scriptService;
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

	public GameService getGameService() {
		return gameService;
	}

	public GameConfig getGameConfig() {
		return gameConfig;
	}
}
