package org.mmo.gate.service;

import com.google.protobuf.Message;
import org.mmo.engine.server.ServerInfo;
import org.mmo.engine.util.math.MathUtil;
import org.mmo.gate.struct.LoginServerInfo;
import org.mmo.message.AccountServiceGrpc;
import org.mmo.message.ServerListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连接登录服rpc
 * <br>
 * 有多个rpc服务器
 *
 * @author jzy
 */
@Service
public class GateToLoginRpcService {
    public static final Logger LOGGER= LoggerFactory.getLogger(GateToLoginRpcService.class);
    //登陆服列表
    private Map<Integer, LoginServerInfo> loginServerInfoMap=new ConcurrentHashMap<>();


    /**
     * 更新登陆服信息
     * @param value
     */
    public void updateLoginServer(ServerListResponse value){
        if(value.getServerCount()<1){
            LOGGER.warn("无可用登陆服");
        }

        value.getServerList().forEach(it->{
           // LOGGER.debug("登陆服信息：{}",it.toString());
            LoginServerInfo loginServerInfo = loginServerInfoMap.get(it.getId());
            if(loginServerInfo==null){
                loginServerInfo=new LoginServerInfo();
                loginServerInfoMap.put(it.getId(),loginServerInfo);
                ServerInfo serverInfo=new ServerInfo();
                loginServerInfo.setServerInfo(serverInfo);
                serverInfo.setWwwip(it.getWwwip());
                loginServerInfo.connectLogin();
            }
            ServerInfo serverInfo = loginServerInfo.getServerInfo();
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

    /**
     * 随机选择发送消息
     */
    public AccountServiceGrpc.AccountServiceStub randomAccountStub(){
        if(loginServerInfoMap.size()<1){
            return null;
        }
        LoginServerInfo login = MathUtil.random(loginServerInfoMap.values());
        AccountServiceGrpc.AccountServiceStub accountStub = login.getAccountStub();
        if(accountStub==null){
            login.connectLogin();
        }
        return accountStub;
    }


}
