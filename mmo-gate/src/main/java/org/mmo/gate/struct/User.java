package org.mmo.gate.struct;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.mmo.engine.io.message.IdMessage;
import org.mmo.engine.io.message.MsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * 用户连接信息
 *
 * @author jzy
 */
public class User {
    public static final Logger LOGGER = LoggerFactory.getLogger(User.class);

    /**
     * 所在服务器
     */
    private int gameId;
    /**
     * 用户id
     */
    private long userId;
    /**
     * 玩家唯一id
     */
    private long playerId;

    /**
     * 客戶端连接会话
     */
    private Channel clientChannel;
    /**
     * 玩家连接的游戏服会话
     */
    private Channel gameChannel;

    /**
     * 客户端会话id
     */
    private long channelId;
    // 账号
    private String account;
    // ip
    private String ip;

    /**
     * 缓存发送消息包
     */
    private List<byte[]> packMessages = new ArrayList<>(50);

    /**
     * 缓存消息长度
     */
    private int packMessageLength;

    /**
     * 调度返回
     */
    private ScheduledFuture<?> packMessageFuture;

    /**
     * 消息加密
     */
    private RC4 rc4;


    public User(Channel clientChannel) {
        this.clientChannel = clientChannel;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public Channel getClientChannel() {
        return clientChannel;
    }

    public void setClientChannel(Channel clientChannel) {
        this.clientChannel = clientChannel;
    }

    public Channel getGameChannel() {
        return gameChannel;
    }

    public void setGameChannel(Channel gameChannel) {
        this.gameChannel = gameChannel;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<byte[]> getPackMessages() {
        return packMessages;
    }

    public void setPackMessages(List<byte[]> packMessages) {
        this.packMessages = packMessages;
    }

    public int getPackMessageLength() {
        return packMessageLength;
    }

    public void setPackMessageLength(int packMessageLength) {
        this.packMessageLength = packMessageLength;
    }

    public ScheduledFuture<?> getPackMessageFuture() {
        return packMessageFuture;
    }

    public void setPackMessageFuture(ScheduledFuture<?> packMessageFuture) {
        this.packMessageFuture = packMessageFuture;
    }

    public RC4 getRc4() {
        return rc4;
    }

    public void setRc4(RC4 rc4) {
        this.rc4 = rc4;
    }

    /**
     * 发送缓存的消息
     */
    public void sendToUser() {
        if (!clientChannel.isActive()) {
            LOGGER.warn("客户端连接会话未空");
            return;
        }
        byte[][] sendBytes = packMessages.toArray(new byte[packMessages.size()][]);
        clientChannel.writeAndFlush(Unpooled.wrappedBuffer(sendBytes));
        packMessages.clear();
        setPackMessageLength(0);
    }

    /**
     * 给前端发送消息
     *
     * @note 发送byte[]和Message类型
     * @param msg
     * @return
     */
    public boolean sendToUser(Object msg) {
        try {
            return MsgUtil.sendClientMsg(clientChannel, msg);
        } catch (Exception e) {
            LOGGER.error("sendToUser:", e);
        }
        return false;
    }

    /**
     * 发往游戏服
     *
     * @param data
     * @param msgId
     */
    public void sendToGame(byte[] data, int msgId) {
        if (userId < 1 || gameChannel == null) {
            LOGGER.warn("连接{}未登录，消息{}转发失败", MsgUtil.getIp(clientChannel), msgId);
            return;
        }
        IdMessage idMessage = IdMessage.newIDMessage(gameChannel, data, playerId < 1 ? userId : playerId, msgId);
        gameChannel.writeAndFlush(idMessage);

    }


}
