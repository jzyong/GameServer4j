package org.mmo.game.script.server;


import io.grpc.stub.StreamObserver;
import org.mmo.common.constant.ServerType;
import org.mmo.common.scripts.IServerScript;
import org.mmo.engine.server.ServerProperties;
import org.mmo.game.service.GameManager;
import org.mmo.message.ServerInfo;
import org.mmo.message.ServerRegisterUpdateRequest;
import org.mmo.message.ServerRegisterUpdateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 服務器脚本
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public class GameServerScript implements IServerScript {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameServerScript.class);
    ServerRegisterUpdateRequest.Builder register = ServerRegisterUpdateRequest.newBuilder();
    ServerInfo.Builder serverInfo = ServerInfo.newBuilder();

    @Override
    public void updateServerInfo() {

        try {
            ServerProperties serverProperties = GameManager.getInstance().getServerProperties();
            serverInfo.setId(serverProperties.getId());
            serverInfo.setType(ServerType.GAME.ordinal());
            serverInfo.setState(1);
            serverInfo.setVersion(String.valueOf(serverProperties.getVersion()));
            serverInfo.setIp(serverProperties.getIp());
            serverInfo.setName(serverProperties.getName());
            serverInfo.setWwwip(serverProperties.getIp());

            register.setServerInfo(serverInfo.build());
            serverInfo.clear();
            GameManager.getInstance().getGameToClusterRpcService().getStub().serverUpdate(register.build(), new StreamObserver<ServerRegisterUpdateResponse>() {
                @Override
                public void onNext(ServerRegisterUpdateResponse value) {
                    LOGGER.debug("cluster 状态：{}", value.getStatus());
                }
                @Override
                public void onError(Throwable t) {
                    LOGGER.warn("注册中心移除");
                }
                @Override
                public void onCompleted() {

                }
            });
        } catch (Exception e) {
            LOGGER.error("定时注册", e);
        }


    }
}
