package org.mmo.login.script;


import io.grpc.stub.StreamObserver;
import org.mmo.common.constant.ServerType;
import org.mmo.common.scripts.IServerScript;
import org.mmo.engine.io.netty.config.NettyProperties;
import org.mmo.engine.server.ServerProperties;
import org.mmo.login.service.LoginManager;
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
public class LoginServerScript implements IServerScript {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServerScript.class);
    ServerRegisterUpdateRequest.Builder register = ServerRegisterUpdateRequest.newBuilder();
    ServerInfo.Builder serverInfo = ServerInfo.newBuilder();

    @Override
    public void updateServerInfo() {

        try {
            ServerProperties serverProperties = LoginManager.getInstance().getServerProperties();
            serverInfo.setId(serverProperties.getId());
            serverInfo.setType(ServerType.Login.ordinal());
            serverInfo.setState(1);
            serverInfo.setVersion(String.valueOf(serverProperties.getVersion()));
            serverInfo.setIp(serverProperties.getIp());
            serverInfo.setName(serverProperties.getName());
            int openRpcPort = LoginManager.getInstance().getRpcProperties().getServerPort();
            serverInfo.setPort(openRpcPort);
            serverInfo.setWwwip(serverProperties.getIp()+":"+openRpcPort);

            register.setServerInfo(serverInfo.build());
            serverInfo.clear();
            LoginManager.getInstance().getLoginToClusterRpcService().getStub().serverUpdate(register.build(), new StreamObserver<ServerRegisterUpdateResponse>() {
                @Override
                public void onNext(ServerRegisterUpdateResponse value) {
                  //  LOGGER.debug("cluster 状态：{}", value.getStatus());
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
