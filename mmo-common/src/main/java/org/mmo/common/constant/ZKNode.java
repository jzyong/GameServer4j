package org.mmo.common.constant;

/**
 * zookeeper路径枚举
 * @author jzy
 */
public enum ZKNode {


    /**
     * 服务路径 profile
     */
    ServicePath("/mmo/%s/service"),

    /**
     * 后台web配置 profile serverId
     * {@link org.mmo.common.config.server.ManageConfig}
     */
    ManageConfig("/mmo/%s/manage%s"),
    /**
     * 网关配置 profile serverId
     * {@link org.mmo.common.config.server.GateConfig}
     */
    GateConfig("/mmo/%s/gate%s"),

    /**
     * 登陆服配置 profile serverId
     * {@link org.mmo.common.config.server.LoginConfig}
     */
    LoginConfig("/mmo/%s/login%s"),

    /**
     * 游戏mongo数据库
     * {@link org.mmo.common.config.server.MongoConfig}
     */
    MongoGameConfig("/mmo/%s/mongo/game"),

    /**
     * 策划excel mongo数据库
     * {@link org.mmo.common.config.server.MongoConfig}
     */
    MongoExcelConfig("/mmo/%s/mongo/excel"),


    ;
    private final String key;

    private ZKNode(String key) {
        this.key = key;
    }

    public String getKey(Object... objects) {
        return String.format(key, objects);
    }

}
