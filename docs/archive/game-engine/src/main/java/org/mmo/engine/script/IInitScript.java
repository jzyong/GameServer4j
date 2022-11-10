package org.mmo.engine.script;

/**
 * 初始化脚本
 * 
 * @author JiangZhiYong
 * @date 2018/12/19
 */
public interface IInitScript extends IScript {
    /**
     * 初始化
     */
    void init();

    /** 消耗 */
    default void destroy() {

    }

}
