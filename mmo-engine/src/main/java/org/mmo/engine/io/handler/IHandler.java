package org.mmo.engine.io.handler;

import io.netty.channel.Channel;

/**
 * 消息处理接口
 * @author JiangZhiYong
 * @date 2019/04/11
 */
public interface IHandler extends Runnable {

    Channel getChannel();

    void setChannel(Channel channel);

    Object getMessage();

    void setMessage(Object message);

    long getCreateTime();

    void setCreateTime(long createTime);
    
    public byte[] getMsgBytes();

    public void setMsgBytes(byte[] msgBytes);
}
