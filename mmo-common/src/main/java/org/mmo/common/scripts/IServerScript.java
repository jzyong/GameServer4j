package org.mmo.common.scripts;


import org.mmo.engine.script.IScript;

/**
 * 服务器脚本
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public interface IServerScript extends IScript {

    /**
     * 更新服务器状态信息
     */
    void updateServerInfo();
}
