package org.mmo.engine.io.netty.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Deprecated
@Component
@ConfigurationProperties("netty")
public class NettyProperties {

    /**
     * 服务器配置
     * <p>
     * netty.serverConfigs[0].name=cluster-tcp<br>
     * netty.serverConfigs[0].port=8100
     * netty.serverConfigs[1].name=cluster-http<br>
     * netty.serverConfigs[1].port=8200
     * </p>
     */
    private List<NettyServerConfig> serverConfigs = new ArrayList<>();


    /**
     * 客户端配置
     */
    public List<NettyClientConfig> clientConfigs = new ArrayList<>();


    public List<NettyServerConfig> getServerConfigs() {
        return serverConfigs;
    }

    public void setServerConfigs(List<NettyServerConfig> serverConfigs) {
        this.serverConfigs = serverConfigs;
    }

    public List<NettyClientConfig> getClientConfigs() {
        return clientConfigs;
    }

    public void setClientConfigs(List<NettyClientConfig> clientConfigs) {
        this.clientConfigs = clientConfigs;
    }
}
