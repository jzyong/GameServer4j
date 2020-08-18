package org.mmo.common.scripts;


import org.mmo.engine.script.IScript;
import org.mmo.message.ServerRegisterUpdateRequest;

/**
 * 服务器脚本
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public interface IServerScript extends IScript {

    /**
     * 更新服务器状态信息
     */
    void updateServerInfo();

    /**
     * 构建服务器更新消息
     * @return
     */
    default ServerRegisterUpdateRequest buildServerRegisterUpdateRequest() {
        return ServerRegisterUpdateRequest.newBuilder().build();
    }
}
