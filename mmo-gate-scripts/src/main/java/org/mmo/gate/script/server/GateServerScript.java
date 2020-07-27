package org.mmo.gate.script.server;


import io.grpc.stub.StreamObserver;
import org.mmo.common.constant.ServerType;
import org.mmo.common.scripts.IServerScript;
import org.mmo.engine.io.netty.config.NettyProperties;
import org.mmo.engine.server.ServerProperties;
import org.mmo.gate.service.GateManager;
import org.mmo.message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 服務器脚本
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public class GateServerScript implements IServerScript {
    private static final Logger LOGGER = LoggerFactory.getLogger(GateServerScript.class);
    ServerRegisterUpdateRequest.Builder register = ServerRegisterUpdateRequest.newBuilder();
    ServerInfo.Builder serverInfo = ServerInfo.newBuilder();

    @Override
    public void updateServerInfo() {

        try {

            //更新本地信息到到注册中心
            ServerProperties serverProperties = GateManager.getInstance().getServerProperties();
            NettyProperties nettyProperties = GateManager.getInstance().getNettyProperties();
            serverInfo.setId(serverProperties.getId());
            serverInfo.setType(ServerType.GATE.ordinal());
            serverInfo.setState(1);
            serverInfo.setVersion(String.valueOf(serverProperties.getVersion()));
            serverInfo.setIp(serverProperties.getIp());
            serverInfo.setName(serverProperties.getName());
            serverInfo.setPort(nettyProperties.getServerConfigs().get(0).getPort());
            serverInfo.setWwwip(serverProperties.getWwwip());
            register.setServerInfo(serverInfo.build());
            serverInfo.clear();
            GateManager.getInstance().getGateToClusterRpcService().getStub().serverUpdate(register.build(), new StreamObserver<ServerRegisterUpdateResponse>() {
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

            //拉取登陆服列表
            GateManager.getInstance().getGateToClusterRpcService().getStub().serverList(ServerListRequest.newBuilder().setType(ServerType.Login.getType()).build(), new StreamObserver<ServerListResponse>() {
                @Override
                public void onNext(ServerListResponse value) {
                    GateManager.getInstance().getGateToLoginRpcService().updateLoginServer(value);
                }

                @Override
                public void onError(Throwable t) {
                    LOGGER.warn("连接cluster失败,{}",t.getMessage());
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
