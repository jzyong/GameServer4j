package org.jzy.game.gate.tcp.game;


import com.jzy.javalib.base.util.IdUtil;
import com.jzy.javalib.base.util.SymbolUtil;
import com.jzy.javalib.network.netty.config.NettyServerConfig;
import com.jzy.javalib.network.netty.tcp.TcpServer;
import com.jzy.javalib.network.netty.tcp.TcpService;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import org.jzy.game.common.config.server.GateConfig;
import org.jzy.game.common.constant.ServerType;
import org.jzy.game.gate.struct.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 游戏 tcp 通信
 */
@Service
public class GameTcpService extends TcpService {
    private static final Logger LOG = LoggerFactory.getLogger(GameTcpService.class);

    private TcpServer nettyServer;

    @Autowired
    private GameTcpChannelInitializer tcpChannelInitializer;
    @Autowired
    private GateConfig gateConfig;

    /**
     * 游戏服务器 type serverId
     */
    private final Map<Integer, Map<Integer, GameServerInfo>> gameServers = new ConcurrentHashMap<>();

    /**
     * 大厅服务器 key：ip hash值
     */
    private final SortedMap<Integer, GameServerInfo> hallHashServers = new TreeMap<>();

    public GameTcpService() {
    }


    @PostConstruct
    public void start() {
        LOG.debug(" run game tcp ... ");
        nettyServer=new TcpServer();
        NettyServerConfig nettyServerConfig = new NettyServerConfig();
        nettyServerConfig.setPort(gateConfig.getGameTcpPort());
        nettyServer.setNettyServerConfig(nettyServerConfig);
        nettyServer.setChannelInitializer(tcpChannelInitializer);
        nettyServer.start();
    }

    @PreDestroy
    public void stop() {
        LOG.debug(" stop game tcp ... ");
        nettyServer.stop();
    }

    /**
     * 游戏服信息
     *
     * @param serverType
     * @param serverId
     * @return
     */
    public GameServerInfo getGameServerInfo(int serverType, int serverId) {
        Map<Integer, GameServerInfo> map = gameServers.get(serverType);
        if (map == null) {
            return null;
        }
        return map.get(serverId);
    }

    /**
     * 添加游戏连接信息
     * @param gameServerInfo
     */
    public void addGameServerInfo(GameServerInfo gameServerInfo) {
        Map<Integer, GameServerInfo> map = gameServers.get(gameServerInfo.getServerType());
        if (map == null) {
            map = new TreeMap<>();
            gameServers.put(gameServerInfo.getServerType(), map);
        }
        // 加入大厅路由容器
        if (gameServerInfo.getServerType() == ServerType.Hall.getType()) {
            int hash = IdUtil.getHash(gameServerInfo.getIp() + SymbolUtil.DENGHAO + gameServerInfo.getId());
            hallHashServers.put(hash, gameServerInfo);
            LOGGER.info("大厅服【加入】IP：{} ID：{} HASH:{}", gameServerInfo.getIp(), gameServerInfo.getId(), hash);
        }
        map.put(gameServerInfo.getId(), gameServerInfo);
    }

    /**
     * 移除服务器连接
     *
     * @param gameServerInfo
     */
    public void removeGameServerInfo(GameServerInfo gameServerInfo) {
        if (gameServerInfo == null) {
            return;
        }
        Map<Integer, GameServerInfo> map = gameServers.get(gameServerInfo.getServerType());
        if (map == null) {
            return;
        }
        map.remove(gameServerInfo.getId());
        // 移除大厅路由容器
        if (gameServerInfo.getServerType() == ServerType.Hall.getType()) {
            int hash = IdUtil.getHash(gameServerInfo.getIp() + SymbolUtil.DENGHAO + gameServerInfo.getId());
            hallHashServers.remove(hash);
            LOGGER.info("大厅服【移除】IP：{} ID：{} HASH:{}", gameServerInfo.getIp(), gameServerInfo.getId(), hash);
        }
        LOGGER.info("服务器{} {}-{} 断开连接", gameServerInfo.getServerType(), gameServerInfo.getId(), gameServerInfo.getIp());
    }

    @Override
    public synchronized void onChannelClosed(Channel channel) {
        super.onChannelClosed(channel);
        Attribute<GameServerInfo> attr = channel.attr(GameTcpServerHandler.GameServerInfo);
        if (attr != null && attr.get() != null) {
            removeGameServerInfo(attr.get());
        }
    }

    /**
     * 为新连接用户分配大厅
     * <p>
     * 根据用户账户进行路由，移动网络玩家ip可能存在改变，导致重连进入不同的大厅服
     * </p>
     *
     * @return
     */
    public GameServerInfo assignHall(User user) {
        if (hallHashServers.isEmpty()) {
            LOGGER.warn("没有可用大厅服务器");
            return null;
        }

        int hash = IdUtil.getHash(user.getAccount());
        // 得到大于该Hash值的所有Map
        SortedMap<Integer, GameServerInfo> subMap = hallHashServers.tailMap(hash);
        int index = 0;
        if (subMap.isEmpty()) {
            index = hallHashServers.firstKey();
        } else {
            index = subMap.firstKey();
        }
        GameServerInfo gameServerInfo = hallHashServers.get(index);
        LOGGER.info("账号:{}-{} 分配服务器：{}-{}-{}", user.getAccount(), hash, gameServerInfo.getId(), gameServerInfo.getIp(),
                index);
        return gameServerInfo;
    }


}
