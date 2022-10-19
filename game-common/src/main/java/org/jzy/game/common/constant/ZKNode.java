package org.jzy.game.common.constant;

import org.jzy.game.common.config.server.*;

/**
 * zookeeper路径枚举
 * @author jzy
 */
public enum ZKNode {


    /**
     * 服务路径 profile
     */
    ServicePath("/game/%s/service"),

    /**
     * 日志 kafka 地址
     */
    LogKafkaUrl("/game/%s/log/kafka/url"),

    /**
     * 后台web配置 profile serverId
     * {@link org.jzy.game.common.config.server.ManageConfig}
     */
    ManageConfig("/game/%s/manage%s"),
    /**
     * 网关配置 profile serverId
     * {@link org.jzy.game.common.config.server.GateConfig}
     */
    GateConfig("/game/%s/gate%s"),

    /**
     * api服配置 profile serverId
     * {@link ApiConfig}
     */
    ApiConfig("/game/%s/api%s"),

    /**
     * 游戏服配置 profile serverId
     * {@link HallConfig}
     */
    HallConfig("/game/%s/hall%s"),


    /**
     * 策划excel mongo数据库
     * {@link MongoConfig}
     */
    MongoExcelConfig("/game/%s/mongo/excel"),



    ;
    private final String key;

    private ZKNode(String key) {
        this.key = key;
    }

    public String getKey(Object... objects) {
        return String.format(key, objects);
    }

}
