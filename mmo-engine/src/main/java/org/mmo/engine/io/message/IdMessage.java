package org.mmo.engine.io.message;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * 客户端消息处理类,为每个消息添加一个ID标识
 */
public final class IdMessage implements Runnable {


    public static IdMessage newIDMessage(Object msg) {
        return new IdMessage(msg);
    }


    public static IdMessage newIDMessage(Channel channel, Object msg, long pid, int msgId) {
        return new IdMessage(channel, msg, pid,msgId);
    }

    /**纯消息内容*/
    private Object msg;
    /**唯一编号*/
    private long id;
    private Channel channel;
    /**消息唯一编号*/
    private int msgId;

    private IdMessage(Object msg) {
        this.msg = msg;
    }

    /**
     *
     * @param channel
     * @param msg byte[]
     * @param id
     */
    private IdMessage(Channel channel, Object msg, long id, int msgId) {
        if (msg instanceof Message || msg instanceof ByteBuf || msg instanceof byte[]) {
            this.msg = msg;
            this.id = id;
            this.channel = channel;
            this.msgId=msgId;
        } else {
            throw new RuntimeException("数据类型错误：" + msg.getClass().getName());
        }
    }

    public long getId() {
        return id;
    }

    public Channel getSession() {
        return channel;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    @Override
    public void run() {
        MsgUtil.sendClientMsg(channel, this);
    }

    public Object getMsg() {
        return msg;
    }
}
