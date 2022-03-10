package org.jzy.game.common.config.server;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * 服务配置
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@JsonRootName("details")
public class ServiceConfig {

    /**
     * 服务器配置信息 json字符串
     */
    private String serverConfig;

    /**
     * 反序列化后的对象
     */
    private transient Object configObject;

    public String getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(String serverConfig) {
        this.serverConfig = serverConfig;
    }

    public <T> T getConfig(Class<T> tClass) {
        if (configObject == null) {
            configObject = JSON.parseObject(serverConfig, tClass);
        }
        return (T) configObject;
    }

}
