package org.mmo.engine.io.handler;

import com.google.protobuf.Message;
import io.netty.channel.Channel;
import org.mmo.engine.io.message.MsgUtil;

public abstract class TcpHandler implements IHandler{
    protected Channel channel;       // 消息来源
    protected Message message;       // 请求消息
    protected long createTime;      //创建时间
    protected long pid;             //角色|用户唯一ID
    private byte[] msgBytes;       //去除本消息长度后剩下的数据，可能有id等，根据协议确定


    /**
     * 返回消息
     * @param msg
     */
    public void sendInnerMsg(int mid, Message msg) {
        try {
            MsgUtil.sendInnerMsg(channel, msg, pid,mid);
        } catch (Exception e) {
           e.printStackTrace();
        }
        
    }

    /**直接返回消息
     * @param msg
     */
    public void sendMsg(Message msg) {
        if (getChannel() == null || !getChannel().isActive()) {
            return;
        }
        MsgUtil.sendClientMsg(channel, msg);
    }


    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Message getMessage() {
        return this.message;
    }

    /** 获取消息 */
    @SuppressWarnings("unchecked")
    public <T extends Message> T getMsg() {
        return (T) message;
    }

    public void setMessage(Object message) {
        if (message instanceof Message) {
            this.message = (Message) message;
        }
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public byte[] getMsgBytes() {
        return msgBytes;
    }

    public void setMsgBytes(byte[] msgBytes) {
        this.msgBytes = msgBytes;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long uid) {
        this.pid = uid;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int)(createTime ^ (createTime >>> 32));
        result = prime * result + (int)(pid ^ (pid >>> 32));
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TcpHandler other = (TcpHandler)obj;
        if (createTime != other.createTime)
            return false;
        if (pid != other.pid)
            return false;
        return true;
    }
    
    
}
