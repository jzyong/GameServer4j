package org.mmo.manage.service;

import org.mmo.common.config.server.GameConfig;
import org.mmo.common.constant.GlobalProperties;
import org.mmo.common.constant.ServerType;
import org.mmo.common.constant.ZKNode;
import org.mmo.common.service.ZkClientService;
import org.mmo.common.struct.server.GameServerInfo;
import org.mmo.common.struct.server.LoginServerInfo;
import org.mmo.message.ServerServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * rpc服务管理
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Service
public class RpcClientService {
    public static final Logger LOGGER = LoggerFactory.getLogger(RpcClientService.class);


    private List<GameServerInfo> gameServerInfos = new ArrayList<>();

    private List<LoginServerInfo> loginServerInfos = new ArrayList<>();

    @Autowired
    private ZkClientService zkClientService;

    @Autowired
    private GlobalProperties globalProperties;


    /**
     * 获取游戏服信息
     *
     * @param serverId
     * @return
     */
    public GameServerInfo getGameServerInfo(int serverId) {
        Optional<GameServerInfo> any = gameServerInfos.stream().filter(it -> it.getId() == serverId).findAny();
        if (any.isPresent()) {
            return any.get();
        } else {
            GameConfig gameConfig = zkClientService.getConfig(ZKNode.GameConfig.getKey(globalProperties.getProfile(), String.valueOf(serverId)), GameConfig.class);
            if (gameConfig == null) {
                return null;
            }
            GameServerInfo gameServerInfo = new GameServerInfo(serverId);
            gameServerInfo.setUrl(gameConfig.buildRpcUrl());
            gameServerInfo.connectGame();
            gameServerInfos.add(gameServerInfo);
            return gameServerInfo;
        }
    }

    /**
     * 获取登录服信息
     *
     * @param serverId
     * @return
     */
    public LoginServerInfo getLoginServerInfo(int serverId) {
        Optional<LoginServerInfo> any = loginServerInfos.stream().filter(it -> Integer.parseInt(it.getId()) == serverId).findAny();
        if (any.isPresent()) {
            return any.get();
        } else {
            GameConfig gameConfig = zkClientService.getConfig(ZKNode.LoginConfig.getKey(globalProperties.getProfile(), String.valueOf(serverId)), GameConfig.class);
            if (gameConfig == null) {
                return null;
            }
            LoginServerInfo loginServerInfo = new LoginServerInfo();
            loginServerInfo.setId(String.valueOf(serverId));
            loginServerInfo.setUrl(gameConfig.buildRpcUrl());
            loginServerInfo.connectLogin();
            loginServerInfos.add(loginServerInfo);
            return loginServerInfo;
        }
    }

    /**
     * 获取同步调用rpc
     *
     * @param serverType
     * @param serverId
     * @return
     */
    public ServerServiceGrpc.ServerServiceBlockingStub getServerServiceBlocking(ServerType serverType, int serverId) {
        switch (serverType) {
            case GAME -> {
                GameServerInfo gameServerInfo = getGameServerInfo(serverId);
                if (gameServerInfo == null) {
                    return null;
                }
                return gameServerInfo.getServerServiceBlockingStub();
            }
            case GATE -> {
                LOGGER.warn("not finish");
                //TODO
                return null;
            }
            case LOGIN -> {
                LoginServerInfo loginServerInfo = getLoginServerInfo(serverId);
                if (loginServerInfo == null) {
                    return null;
                }
                return loginServerInfo.getServerServiceBlockingStub();
            }
        }
        return null;
    }


}
