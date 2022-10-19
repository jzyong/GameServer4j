package org.jzy.game.gate.struct;

import com.google.protobuf.Message;
import com.jzy.javalib.network.io.message.IdMessage;
import com.jzy.javalib.network.io.message.MsgUtil;
import com.jzy.javalib.network.io.message.UserMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.jzy.game.gate.service.GateManager;
import org.jzy.game.proto.MID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

/**
 * 用户连接信息
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
public class User {
    public static final Logger LOGGER = LoggerFactory.getLogger(User.class);

    /**
     * 所在服务器
     */
    private int gameId;
    /**
     * 用户id 暂时和玩家id一致,如果一个账号需要对应多个玩家，需要修改
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
    private Channel hallChannel;

    /**
     * 客户端会话id
     */
    private long channelId;
    // 账号
    private String account;
    // ip
    private String ip;

    /**
     * 缓存发送消息包，用于消息包合并
     */
    private List<ByteBuf> packMessages = new ArrayList<>();

    /**
     * 缓存消息长度
     */
    private int packMessageLength;

    /**
     * 调度返回
     */
    private ScheduledFuture<?> packMessageFuture;

    /**
     * 最新消息序号
     */
    private int msgSequence;

    /**
     * 客户端确认消息序号
     */
    private int ackMsgSequence;

    /**
     * 缓存的客户端消息
     */
    private LinkedList<UserMessage> cacheMessages = new LinkedList<>();

    /**
     * 消息加密
     */
    private RC4 rc4;

    /**
     * 心跳时间 (检测离线等)
     */
    private long heartTime;


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
        return hallChannel;
    }

    public void setGameChannel(Channel gameChannel) {
        this.hallChannel = gameChannel;
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

    public List<ByteBuf> getPackMessages() {
        return packMessages;
    }

    public void setPackMessages(List<ByteBuf> packMessages) {
        this.packMessages = packMessages;
    }

    public int getMsgSequence() {
        return msgSequence;
    }

    public void setMsgSequence(int msgSequence) {
        this.msgSequence = msgSequence;
    }

    public int getAckMsgSequence() {
        return ackMsgSequence;
    }

    public void setAckMsgSequence(int ackMsgSequence) {
        this.ackMsgSequence = ackMsgSequence;
    }

    public LinkedList<UserMessage> getCacheMessages() {
        return cacheMessages;
    }

    public void setCacheMessages(LinkedList<UserMessage> cacheMessages) {
        this.cacheMessages = cacheMessages;
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

    public long getHeartTime() {
        return heartTime;
    }

    public void setHeartTime(long heartTime) {
        this.heartTime = heartTime;
    }

    /**
     * 发送缓存的消息
     */
    public void sendToUser() {
        LOGGER.debug("{} 发送合并消息长度：{}",this.playerId,this.packMessageLength);
        var sendBytes = packMessages.toArray(new ByteBuf[packMessages.size()]);
        clientChannel.writeAndFlush(Unpooled.wrappedBuffer(sendBytes));
        packMessages.clear();
        setPackMessageLength(0);
    }

    /**
     *
     * @param msgId
     * @param protoBytes
     * @return
     */
    public boolean sendToUser(int msgId, int msgSequence, byte[] protoBytes) {
        try {
            if (clientChannel == null || !clientChannel.isActive()) {
                LOGGER.debug("{} 连接已经断开,序列号{}消息{} {} 发送失败", playerId, msgSequence, msgId, MID.forNumber(msgId));
                return false;
            }

            int length = MsgUtil.ClientHeaderExcludeLength + protoBytes.length;
            ByteBuf byteBuf = Unpooled.buffer(length + 4);
            byteBuf.writeIntLE(length);
            byteBuf.writeIntLE(msgId);
            byteBuf.writeIntLE(0);
            byteBuf.writeIntLE(msgSequence);
            byteBuf.writeBytes(protoBytes);
            return MsgUtil.sendClientMsg(clientChannel, byteBuf, msgSequence);
        } catch (Exception e) {
            LOGGER.error("sendToUser:", e);
        }
        return false;
    }

    /**
     * 是否发送缓存的消息
     *
     * @param msgSequence 请求序号
     * @return true 给客户端发送缓存未接取的消息
     */
    public boolean sendCacheMessage(int msgSequence, int requestMessageId) {
        if (msgSequence < 1) {
            return false;
        }
        try {
            if (this.msgSequence >= msgSequence) {
                Optional<UserMessage> any = cacheMessages.stream().filter(it -> it.getMsgSequence() == msgSequence)
                        .findAny();
                if (any.isPresent()) {
                    UserMessage userMessage = any.get();
                    sendToUser(userMessage.getMsgId(), userMessage.getMsgSequence(), userMessage.getMsgBytes());
                    LOGGER.debug("{} 获取缓存消息：{}->{} {}", playerId, requestMessageId, userMessage.getMsgId(),
                            msgSequence);
                    return true;
                } else {
                    LOGGER.info("{} 缓存队列不存在消息{}-{}，向逻辑服请求从新执行 缓存数：{} 当前序号：{}", playerId, requestMessageId, msgSequence,
                            this.cacheMessages.size(), this.msgSequence);
                }
            }
        } catch (Exception e) {
            LOGGER.error("sendCacheMessage", e);
        } finally {
            if (this.msgSequence < msgSequence) {
                this.msgSequence = msgSequence;
            }

        }
        return false;
    }

    /**
     * 接收大厅和子游戏返回的消息
     *
     * @param msgId
     * @param msgSequence
     * @param protoBytes
     */
    public void receiveUserMessage(int msgId, int msgSequence, byte[] protoBytes) {
        // 使用玩家线程处理
        clientChannel.eventLoop().execute(() -> {
            // 有序号的协议需要缓存
            if (msgSequence > 0) {
                UserMessage userMessage = null;
                if (cacheMessages.size() > 100) { // 最多缓存100条
                    userMessage = cacheMessages.pollFirst();
                } else {
                    userMessage = new UserMessage();
                }
                userMessage.setMsgBytes(protoBytes);
                userMessage.setMsgId(msgId);
                userMessage.setMsgSequence(msgSequence);
                this.cacheMessages.addLast(userMessage);
            }

            if (!GateManager.getInstance().getGateConfig().isMessageMerge()){
                sendToUser(msgId, msgSequence, protoBytes);
                return;
            }


            // 协议过大，直接发送
            if (protoBytes.length > MsgUtil.MESSAGE_EXPIRE_SIZE) {
                if (packMessages.size() > 0) {
                    sendToUser();
                }
                sendToUser(msgId, msgSequence, protoBytes);
                // 消息包过下，合并一起发送
            } else {
                if (protoBytes.length + packMessageLength > MsgUtil.MESSAGE_EXPIRE_SIZE) {
                    sendToUser();
                }
                // 缓存消息
                // 消息长度4+消息id4+客户端已收到最小序号4+消息序号4+protobuf消息体
                int length = MsgUtil.ClientHeaderExcludeLength + protoBytes.length;
                ByteBuf byteBuf = Unpooled.buffer(length + 4);
                byteBuf.writeIntLE(length);
                byteBuf.writeIntLE(msgId);
                byteBuf.writeIntLE(0);
                byteBuf.writeIntLE(msgSequence);
                byteBuf.writeBytes(protoBytes);
                packMessages.add(byteBuf);
                packMessageLength = MsgUtil.ClientHeaderLength + protoBytes.length + packMessageLength;
            }
        });
    }

    /**
     * 发往游戏服(slots)
     *
     * @param data
     * @param msgId
     */
    public void sendToHall(byte[] data, int msgId, int msgSequence) {
        if (playerId < 1 || hallChannel == null || !hallChannel.isActive()) {
            LOGGER.warn("连接{}未登录，消息{}-{}转发失败:{}", MsgUtil.getIp(clientChannel), msgId, MID.forNumber(msgId),
                    this.hashCode());
            return;
        }
        IdMessage idMessage = IdMessage.newIDMessage(hallChannel, data, playerId < 1 ? -1 : playerId, msgId,
                msgSequence);
        hallChannel.writeAndFlush(idMessage);
    }



    /**
     * 给前端发送消息
     *
     * @param msg
     * @return
     * @note 发送byte[]和Message类型
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
     * 确认消息，请求缓存协议
     *
     * @param ackMsgSequence
     */
    public void ackMessage(int ackMsgSequence) {
        if (this.ackMsgSequence >= ackMsgSequence) {
            return;
        }
        this.ackMsgSequence = ackMsgSequence;
        if (cacheMessages.size() > 1) {
            Iterator<UserMessage> iterator = cacheMessages.iterator();
            while (iterator.hasNext()) {
                UserMessage userMessage = iterator.next();
                if (userMessage.getMsgSequence() < ackMsgSequence) {
                    iterator.remove();
                    LOGGER.debug("玩家：{}移除确认序列号:{} 当前序列号：{}", this.playerId, userMessage.getMsgSequence(),
                            ackMsgSequence);
                } else {
                    break;
                }
            }
            if (cacheMessages.size() > 20) {
                LOGGER.debug("{} 缓存协议个数：{}", playerId, cacheMessages.size());
            }
        }
    }

}
