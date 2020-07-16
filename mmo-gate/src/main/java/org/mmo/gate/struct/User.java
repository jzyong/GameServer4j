package org.mmo.gate.struct;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * 用户连接信息
 * @author jzy
 */
public class User {

    /**
     * 所在服务器
     */
    private int gameId ;
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

    /** 缓存发送消息包 */
    private List<byte[]> packMessages = new ArrayList<>(50);

    /** 缓存消息长度 */
    private int packMessageLength;

    /** 调度返回 */
    private ScheduledFuture<?> packMessageFuture;

    /**消息加密*/
    private RC4 rc4;


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
}
