package org.mmo.game.service;

import org.mmo.common.constant.ServerType;
import org.mmo.engine.io.grpc.RpcClientService;
import org.mmo.engine.server.ServerProperties;
import org.mmo.game.struct.GateServerInfo;
import org.mmo.message.ServerInfo;
import org.mmo.message.ServerListResponse;
import org.mmo.message.ServerRegisterUpdateRequest;
import org.mmo.message.ServerServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连接cluster
 */
@Service
public class GameToClusterRpcService extends RpcClientService {
    private static final Logger LOGGER= LoggerFactory.getLogger(GameToClusterRpcService.class);

    private ServerServiceGrpc.ServerServiceBlockingStub blockingStub;
    private ServerServiceGrpc.ServerServiceStub stub;

    /**
     * 连接的网关服务器
     */
    private final Map<Integer, GateServerInfo> gateServerInfoMap=new ConcurrentHashMap<>();

    @Autowired
    private ServerProperties serverProperties;

    @PostConstruct
    public void init(){

        start();
        blockingStub=ServerServiceGrpc.newBlockingStub(getChannel());
        stub=ServerServiceGrpc.newStub(getChannel());


        var serverInfo= ServerInfo.newBuilder()
                .setId(serverProperties.getId())
                .setType(ServerType.GAME.ordinal())
                .setState(1)
                .setVersion(String.valueOf(serverProperties.getVersion()))
                .build();
        var response= blockingStub.serverRegister(ServerRegisterUpdateRequest.newBuilder().setServerInfo(serverInfo).build());
        LOGGER.info("game 成功注册到 cluster {}",response.toString());
    }

    /**
     * 更新登陆服信息
     * @param value
     */
    public void updateGateServer(ServerListResponse value){
        if(value.getServerCount()<1){
            LOGGER.warn("无可用网关服");
        }

        value.getServerList().forEach(it->{
            // LOGGER.debug("登陆服信息：{}",it.toString());
            GateServerInfo gateServerInfo = gateServerInfoMap.get(it.getId());
            if(gateServerInfo==null){
                gateServerInfo=new GateServerInfo();
                gateServerInfoMap.put(it.getId(),gateServerInfo);
                org.mmo.engine.server.ServerInfo serverInfo=new org.mmo.engine.server.ServerInfo();
                gateServerInfo.setServerInfo(serverInfo);
                serverInfo.setWwwip(it.getWwwip());
                serverInfo.setIp(it.getIp());
                serverInfo.setPort(it.getPort());
                serverInfo.setGateGamePort(it.getGamePort());
                serverInfo.setId(it.getId());
                gateServerInfo.connectGate();
            }
            //可能存在channel为空，注册中心状态更新不及时？
            if(gateServerInfo.getGateTcpClient().getChannel()==null){
                gateServerInfo.connectGate();
            }
            org.mmo.engine.server.ServerInfo serverInfo = gateServerInfo.getServerInfo();
            serverInfo.setId(it.getId());
            serverInfo.setBelongId(it.getBelongID());
            serverInfo.setContent(it.getContent());
            serverInfo.setHttpPort(it.getHttpPort());
            serverInfo.setIp(it.getIp());
            serverInfo.setMaintainTime(it.getMaintainTime());
            serverInfo.setMaxUserCount(it.getMaxUserCount());
            serverInfo.setName(it.getName());
            serverInfo.setOnline(it.getOnline());
            serverInfo.setOpenTime(it.getOpenTime());
            serverInfo.setPort(it.getPort());
            serverInfo.setServerState(it.getState());
            serverInfo.setVersion(it.getVersion());
            serverInfo.setWwwip(it.getWwwip());
        });
    }


    public ServerServiceGrpc.ServerServiceBlockingStub getBlockingStub() {
        return blockingStub;
    }

    public ServerServiceGrpc.ServerServiceStub getStub() {
        return stub;
    }

    public Map<Integer, GateServerInfo> getGateServerInfoMap() {
        return gateServerInfoMap;
    }
}
