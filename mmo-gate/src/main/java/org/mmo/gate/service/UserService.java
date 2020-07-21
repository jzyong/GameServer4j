package org.mmo.gate.service;

import io.netty.channel.Channel;
import org.mmo.common.constant.OfflineType;
import org.mmo.engine.io.message.MsgUtil;
import org.mmo.gate.server.tcp.server.user.UserTcpServerHandler;
import org.mmo.gate.struct.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户管理
 *
 * @author jzy
 */
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    /**
     * 用户id map
     */
    private Map<Long, User> userIds = new ConcurrentHashMap<>();

    /**
     * channel id map
     */
    private Map<Long, User> channelIds = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {

    }

    public User getUserByChannelId(Long channelId) {
        return channelIds.get(channelId);
    }

    public User getUserByUserId(Long userId) {
        return userIds.get(userId);
    }


    /**
     * 用户连接创建
     *
     * @param user
     */
    public void onConnect(User user) {
        user.setChannelId(MsgUtil.getChannelId(user.getClientChannel()));
        user.setIp(MsgUtil.getIp(user.getClientChannel()));
        channelIds.put(user.getChannelId(), user);
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
            //TODO

            //关闭消息压缩回调
            if(user.getPackMessageFuture()!=null){
                user.getPackMessageFuture().cancel(true);
            }
            userIds.remove(user.getUserId());
            channelIds.remove(user.getChannelId());
            clientChannel.close();
            LOGGER.info("{}-{}-{} 离线{}", user.getAccount(), user.getUserId(), offlineType.toString(), MsgUtil.getIp(clientChannel));
        } else {
            LOGGER.warn("{} 无用户信息", MsgUtil.getIp(clientChannel));
        }
    }


}
