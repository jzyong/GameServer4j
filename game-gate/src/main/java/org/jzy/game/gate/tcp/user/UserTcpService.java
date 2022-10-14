package org.jzy.game.gate.tcp.user;


import com.jzy.javalib.network.io.message.MsgUtil;
import com.jzy.javalib.network.netty.config.NettyServerConfig;
import com.jzy.javalib.network.netty.tcp.TcpServer;
import com.jzy.javalib.network.netty.tcp.TcpService;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import org.jzy.game.common.config.server.GateConfig;
import org.jzy.game.gate.struct.SendMergeMessageTimer;
import org.jzy.game.gate.struct.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户 tcp 通信
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Service
public class UserTcpService extends TcpService {
    private static final Logger LOG = LoggerFactory.getLogger(UserTcpService.class);

    private TcpServer nettyServer;

    @Autowired
    private UserTcpChannelInitializer userTcpChannelInitializer;
    @Autowired
    private GateConfig gateConfig;

    /**
     * 发送消息定时器
     */
    private Map<String, SendMergeMessageTimer> sendMergeMessageTimerMap = new HashMap<>();

    public UserTcpService() {
    }


    @PostConstruct
    public void start() {
        LOG.debug(" run user tcp ... ");
        nettyServer = new TcpServer();
        NettyServerConfig nettyServerConfig = new NettyServerConfig();
        nettyServerConfig.setPort(gateConfig.getClientTcpPort());
        nettyServer.setNettyServerConfig(nettyServerConfig);
        nettyServer.setChannelInitializer(userTcpChannelInitializer);
        nettyServer.start(serverBootstrap -> {
            initMessageMergeTimer();
        });
    }

    /**
     * 初始化消息合并定时器
     */
    private void initMessageMergeTimer() {
        if (!gateConfig.isMessageMerge()) {
            return;
        }

        EventLoopGroup eventLoopGroup = nettyServer.getBoot().config().childGroup();
        Iterator<EventExecutor> iterator = eventLoopGroup.iterator();
        int delay = 80;
        while (iterator.hasNext()) {
            EventExecutor eventExecutor = iterator.next();
            if (!(eventExecutor instanceof SingleThreadEventExecutor)) {
                continue;
            }
            SingleThreadEventExecutor executor = (SingleThreadEventExecutor) eventExecutor;
            SendMergeMessageTimer sendMergeMessageTimer = new SendMergeMessageTimer();
            //50ms 合并率比较低，很少消息到达1000字节，下注消息不能全合并
            executor.scheduleAtFixedRate(sendMergeMessageTimer, 5000, delay, TimeUnit.MILLISECONDS);
            var threadName = executor.threadProperties().name();
            sendMergeMessageTimerMap.put(threadName, sendMergeMessageTimer);
            LOGGER.debug("thread：{} add message merge timer", threadName);
            delay++;
        }
    }

    /**
     * 添加消息合并用户
     *
     * @param user
     */
    public void addMergeMessageUser(User user, String threadName) {
        if (!gateConfig.isMessageMerge()) {
            return;
        }
        SendMergeMessageTimer sendMergeMessageTimer = sendMergeMessageTimerMap.get(threadName);
        if (sendMergeMessageTimer == null) {
            LOGGER.warn("线程 {} 不存在，{} 添加消息合并定时器失败", threadName, MsgUtil.getRemoteIpPort(user.getClientChannel()));
            return;
        }
        sendMergeMessageTimer.addUser(user);
    }

    @PreDestroy
    public void stop() {
        LOG.debug(" stop user tcp ... ");
        nettyServer.stop();
    }

}
