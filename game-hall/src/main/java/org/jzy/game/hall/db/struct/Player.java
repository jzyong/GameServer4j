package org.jzy.game.hall.db.struct;

import com.google.protobuf.Message;
import com.jzy.javalib.network.io.message.MsgUtil;
import io.netty.channel.Channel;
import org.jzy.game.common.struct.object.MapObject;
import org.jzy.game.proto.MID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.Document;

import com.alibaba.fastjson.JSON;

/**
 * 玩家数据
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Document(collection = "player")
public class Player extends MapObject {
    public static final Logger LOGGER= LoggerFactory.getLogger(Player.class);
    /**
     * 等级
     */
    private int level;
    /**
     * 经验
     */
    private int exp;

    /**
     * 用户id
     */
    private long userId;

    /**
     * 连接的网关channel
     */
    private transient Channel channel;



    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

    //TODO 序列号
    public void sendMsg(Message message,int messageId){
        if(channel==null||!channel.isActive()){
            LOGGER.warn("失去网关服连接：{} 发送失败", MID.forNumber(messageId));
            return;
        }
        MsgUtil.sendInnerMsg(channel,message,this.getId(),messageId);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
