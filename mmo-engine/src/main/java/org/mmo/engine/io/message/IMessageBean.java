package org.mmo.engine.io.message;


import org.mmo.engine.io.handler.IHandler;

/**
 * 消息构造接口
 */
public interface IMessageBean {

    /**
     * 消息处理线程
     * @return
     */
    String getExecuteThread();

    /**
     * 获取请求处理对象
     *
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    IHandler newHandler() throws InstantiationException, IllegalAccessException;
}
