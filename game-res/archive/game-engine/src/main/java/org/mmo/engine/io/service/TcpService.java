package org.mmo.engine.io.service;

import com.google.protobuf.Message;
import io.netty.channel.Channel;
import org.mmo.engine.io.message.MsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程管理，服务器连接会话管理
 * <br>
 */
public abstract class TcpService implements INettyService<Integer> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(TcpService.class);

    //所有连接
    protected final List<Channel> allChannels =new ArrayList<Channel>();//
    private AtomicInteger channelGetCount=new AtomicInteger();


    public TcpService() {
    }



    public synchronized void onChannelConnect(Channel channel) {
        allChannels.add(channel);
    }

    public synchronized void  onChannelClosed(Channel channel) {
        allChannels.remove(channel);
    }

    public void checkStatus() {
    }


    /**
     * 广播所有连接的消息
     *
     * @param obj
     */
    public void broadcastMsgAllChannel(Object obj) {
        allChannels.forEach(s -> {
            if(obj instanceof Message) {
                //TODO 修改
                MsgUtil.sendInnerMsg(s, (Message)obj, -1,-1);
            }else {
                throw new UnsupportedOperationException(String.format("消息 %s 类型不支持", obj.getClass().getSimpleName()));
            }
           
        });
    }


    public boolean isChannelEmpty() {
        return allChannels.isEmpty();
    }

    /**
     * 服务器内流转的数据都必须带id
     *
     * @param msg
     * @return
     */
    @Override
    public boolean sendMsg(Object msg) {
        Channel channel = nextChannel();
        if (msg instanceof Message) {
            //TODO 获取消息id
               return MsgUtil.sendInnerMsg(channel, (Message)msg, -1,-1);
        }else {
            return MsgUtil.sendInnerMsg(channel, msg);
        }
    }
    
    /**
     * 依次选择channel
     * <br>最好预先分配,内部通信一个channel足以
     */
    public Channel nextChannel() {
    	int count= this.allChannels.size();
    	if(count==1) {
    		return this.allChannels.get(0);
    	}else if(count<1) {
    		return null;
    	}
    	
    	//2的次方
    	if((count&-count)==count) {
    		return this.allChannels.get(channelGetCount.getAndDecrement()&count-1);
    	}else {
    		return this.allChannels.get(Math.abs(channelGetCount.getAndDecrement()%count));
    	}
    }


}
