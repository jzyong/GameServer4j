package org.mmo.game.script.server;


import io.grpc.stub.StreamObserver;
import org.mmo.common.constant.ServerType;
import org.mmo.common.scripts.IServerScript;
import org.mmo.engine.server.ServerProperties;
import org.mmo.game.service.GameManager;
import org.mmo.message.*;
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
        ServerRegisterUpdateRequest serverRegisterUpdateRequest = buildServerRegisterUpdateRequest();
        try {
            //向cluster注册服务器信息
            GameManager.getInstance().getGameToClusterRpcService().getStub().serverUpdate(serverRegisterUpdateRequest, new StreamObserver<ServerRegisterUpdateResponse>() {
                @Override
                public void onNext(ServerRegisterUpdateResponse value) {
//                    LOGGER.debug("cluster 状态：{}", value.getStatus());
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

        //拉取网关服列表
        GameManager.getInstance().getGameToClusterRpcService().getStub().serverList(ServerListRequest.newBuilder().setType(ServerType.GATE.getType()).build(), new StreamObserver<ServerListResponse>() {
            @Override
            public void onNext(ServerListResponse value) {
                GameManager.getInstance().getGameToClusterRpcService().updateGateServer(value);
            }

            @Override
            public void onError(Throwable t) {
                LOGGER.warn("连接Gate失败,{}",t.getMessage());
            }

            @Override
            public void onCompleted() {

            }
        });

        //向网关服注册游戏服
        GameManager.getInstance().getGameToClusterRpcService().getGateServerInfoMap().forEach((k,v)->{
            v.sendMsg(serverRegisterUpdateRequest, MIDMessage.MID.ServerRegisterUpdateReq_VALUE);
        });
    }

    @Override
    public  ServerRegisterUpdateRequest buildServerRegisterUpdateRequest(){
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
        return register.build();
    }
}
