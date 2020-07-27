package org.mmo.gate.struct;

import org.mmo.engine.server.ServerInfo;

/**
 * 登录服务器rpc信息
 * @author jzy
 */
public class LoginServerInfo {
    private ServerInfo serverInfo;
    //TODO 服务调度信息


    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }
}
