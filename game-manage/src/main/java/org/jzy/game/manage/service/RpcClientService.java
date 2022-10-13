package org.jzy.game.manage.service;

import org.jzy.game.common.config.server.ApiConfig;
import org.jzy.game.common.config.server.HallConfig;
import org.jzy.game.common.constant.GlobalProperties;
import org.jzy.game.common.constant.ZKNode;
import org.jzy.game.common.service.ZkClientService;
import org.jzy.game.common.struct.server.ApiServerInfo;
import org.jzy.game.common.struct.server.GameServerInfo;
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

    //TODO 使用微服务
    private List<ApiServerInfo> apiServerInfos = new ArrayList<>();

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
            HallConfig gameConfig = zkClientService.getConfig(ZKNode.HallConfig.getKey(globalProperties.getProfile(), String.valueOf(serverId)), HallConfig.class);
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
    public ApiServerInfo getApiServerInfo(int serverId) {
        Optional<ApiServerInfo> any = apiServerInfos.stream().filter(it -> Integer.parseInt(it.getId()) == serverId).findAny();
        if (any.isPresent()) {
            return any.get();
        } else {
            ApiConfig gameConfig = zkClientService.getConfig(ZKNode.ApiConfig.getKey(globalProperties.getProfile(), String.valueOf(serverId)), ApiConfig.class);
            if (gameConfig == null) {
                return null;
            }
            ApiServerInfo apiServerInfo = new ApiServerInfo();
            apiServerInfo.setId(String.valueOf(serverId));
            apiServerInfo.setUrl(gameConfig.buildRpcUrl());
            apiServerInfo.connectLogin();
            apiServerInfos.add(apiServerInfo);
            return apiServerInfo;
        }
    }

    //TODO
//    /**
//     * 获取同步调用rpc
//     *
//     * @param serverType
//     * @param serverId
//     * @return
//     */
//    public ServerServiceBlockingStub getServerServiceBlocking(ServerType serverType, int serverId) {
//        switch (serverType) {
//            case Hall -> {
//                GameServerInfo gameServerInfo = getGameServerInfo(serverId);
//                if (gameServerInfo == null) {
//                    return null;
//                }
//                return gameServerInfo.getServerServiceBlockingStub();
//                return null;
//            }
//            case GATE -> {
//                LOGGER.warn("not finish");
//                //TODO
//                return null;
//            }
//            case LOGIN -> {
//                LoginServerInfo loginServerInfo = getApiServerInfo(serverId);
//                if (loginServerInfo == null) {
//                    return null;
//                }
//                return loginServerInfo.getServerServiceBlockingStub();
//            }
//        }
//        return null;
//    }


}
