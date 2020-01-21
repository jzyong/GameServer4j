package org.mmo.gate.script.server;


import org.mmo.common.constant.ServerType;
import org.mmo.common.scripts.IServerScript;
import org.mmo.engine.server.ServerProperties;
import org.mmo.gate.service.GateManager;
import org.mmo.message.MIDMessage;
import org.mmo.message.ServerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 服務器脚本
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public class GateServerScript implements IServerScript {
    private static final Logger LOGGER= LoggerFactory.getLogger(GateServerScript.class);
    ServerMessage.ServerRegisterUpdateRequest.Builder register= ServerMessage.ServerRegisterUpdateRequest.newBuilder();
    ServerMessage.ServerInfo.Builder serverInfo= ServerMessage.ServerInfo.newBuilder();

    @Override
    public void updateServerInfo() {
//    	NettyProperties nettyProperties = GateManager.getInstance().getNettyProperties();

        try {
            ServerProperties serverProperties = GateManager.getInstance().getServerProperties();
            serverInfo.setId(serverProperties.getId());
            serverInfo.setType(ServerType.GATE.ordinal());
            serverInfo.setState(1);
            serverInfo.setVersion(String.valueOf(serverProperties.getVersion()));

            register.setServerInfo(serverInfo.build());

            serverInfo.clear();
            //TODO 添加属性

            register.setMsgID(MIDMessage.MID.ServerRegisterUpdateReq);
            GateManager.getInstance().getGateToClusterService().sendMsg(register.build());

        }catch (Exception e){
            LOGGER.error("定时注册",e);
        }


    }
}
