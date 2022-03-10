package org.mmo.common.config.server;

import com.alibaba.fastjson.JSON;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;

/**
 * 网关配置
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Component
@ConfigurationProperties("gate")
public class GateConfig {

    /**
     * 服务器唯一id
     */
    private int id;

    /**
     * 客户端端口
     */
    @Min(1024)
    private int clientTcpPort;

    /**
     * 游戏服端口
     */
    @Min(1024)
    private int gameTcpPort;

    /**
     * ip地址
     */
    private String publicIp;

    /**
     * 内网ip
     */
    private String privateIp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientTcpPort() {
        return clientTcpPort;
    }

    public void setClientTcpPort(int clientTcpPort) {
        this.clientTcpPort = clientTcpPort;
    }

    public int getGameTcpPort() {
        return gameTcpPort;
    }

    public void setGameTcpPort(int gameTcpPort) {
        this.gameTcpPort = gameTcpPort;
    }

    public String getPrivateIp() {
        return privateIp;
    }

    public void setPrivateIp(String privateIp) {
        this.privateIp = privateIp;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}
