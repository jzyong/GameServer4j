package org.jzy.game.gate.service;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.*;
import com.google.protobuf.Message;
import com.jzy.javalib.network.io.message.MsgUtil;
import io.netty.channel.Channel;
import org.jzy.game.common.constant.OfflineType;
import org.jzy.game.gate.struct.User;
import org.jzy.game.gate.tcp.user.UserTcpServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 用户管理
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    /**
     * ip连接数量限制
     */
    public static final int IPConnectLimit = 50;

    /**
     * 玩家id id map
     */
    private Map<Long, User> playerIds = new ConcurrentHashMap<>();


    /**
     * 同一ip连接数统计
     */
    private final LoadingCache<String, Integer> ipConnectCounts = CacheBuilder.newBuilder().maximumSize(5000)
            .concurrencyLevel(Runtime.getRuntime().availableProcessors()).expireAfterWrite(120, TimeUnit.MINUTES)
            .removalListener(new RemovalListener<String, Integer>() {
                @Override
                public void onRemoval(RemovalNotification<String, Integer> notification) {
                    if (notification.getValue() > 10) {
                        LOGGER.info("IP:{} 创建了 {} 个连接", notification.getKey(), notification.getValue());
                    }
                }
            }).build(new CacheLoader<String, Integer>() {
                @Override
                public Integer load(String key) throws Exception {
                    return 0;
                }
            });

    @PostConstruct
    public void init() {

    }


    public User getUserByPlayerId(Long playerId) {
        return playerIds.get(playerId);
    }

    /**
     * 用户连接创建
     *
     * @param user
     */
    public void onSocketConnect(User user) {
        user.setChannelId(MsgUtil.getChannelId(user.getClientChannel()));
        user.setIp(MsgUtil.getIp(user.getClientChannel()));
        int count = addIpCount(user.getIp());
        if (count > IPConnectLimit) {
            List<Long> playerIds = this.playerIds.values().stream().filter(u -> user.getIp().equals(u.getIp()))
                    .map(User::getPlayerId).collect(Collectors.toList());
            LOGGER.warn("IP ：{} 已经超过最大连接数{} {},玩家：{}", user.getIp(), IPConnectLimit, playerIds.size(),
                    JSON.toJSONString(playerIds));
            user.getClientChannel().close();
        }
    }

    /**
     * 玩家加载成功
     *
     * @param user
     */
    public void onUserLoginSuccess(User user, Consumer<User> oldUserConsumer) {
        playerIds.put(user.getPlayerId(), user);
    }

    public Map<Long, User> getPlayerIds() {
        return playerIds;
    }

    /**
     * 玩家离线
     *
     * @param clientChannel
     * @param offlineType
     */
    public void offLine(Channel clientChannel, OfflineType offlineType) {
        User user = clientChannel.attr(UserTcpServerHandler.USER).get();
        if (user != null) {
            if (user.getPlayerId() > 0) {
                User playerUser = getUserByPlayerId(user.getPlayerId());
                if (user == playerUser) {
                    //TODO 进行其他操作，如通知游戏服务器进行退出流程
                    LOGGER.trace("{} 正常退出游戏", user.getPlayerId());
                } else {
                    LOGGER.info("{} 容器是新的玩家，不移除", user.getPlayerId());
                }
            }
            clientChannel.attr(UserTcpServerHandler.USER).set(null);
            clientChannel.close();
            decIpCount(user.getIp());
            LOGGER.info("{} 因 {} 离线", user.getPlayerId(), offlineType.toString());
        } else {
            // 客户端强杀进程，netty先在exceptionCaught 捕获，然后channelInactive捕获，调用两次，会进入此方法
            LOGGER.trace("{} 无用户信息 {}", MsgUtil.getIp(clientChannel), offlineType.toString());
        }
    }

    /**
     * 添加ip计数
     *
     * @param ip
     * @return false 连接数已超过最大允许数
     */
    private int addIpCount(String ip) {
        try {
            int count = ipConnectCounts.get(ip);
            count = count + 1;
            ipConnectCounts.put(ip, count);
            return count;
        } catch (Exception e) {
            LOGGER.error("连接数统计", e);
        }
        return 0;
    }

    /**
     * 减少ip计数
     *
     * @param ip
     * @return
     */
    private int decIpCount(String ip) {
        try {
            int count = ipConnectCounts.get(ip);
            count = count - 1;
            if (count < 1) {
                ipConnectCounts.invalidate(ip);
            } else {
                ipConnectCounts.put(ip, count);
            }
            return count;
        } catch (Exception e) {
            LOGGER.error("连接数统计", e);
        }
        return 0;
    }





}
