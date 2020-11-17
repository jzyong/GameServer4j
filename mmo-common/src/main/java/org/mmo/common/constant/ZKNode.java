package org.mmo.common.constant;

/**
 * @author jzy
 */
public enum ZKNode {


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
    LoginConfig("/mmo/%s/gate%s"),

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
