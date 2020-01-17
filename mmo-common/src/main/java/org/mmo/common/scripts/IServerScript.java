package org.mmo.common.scripts;


import org.mmo.engine.script.IBaseScript;

/**
 * 服务器脚本
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public interface IServerScript extends IBaseScript {

    /**
     * 更新服务器状态信息
     */
    void updateServerInfo();
}
