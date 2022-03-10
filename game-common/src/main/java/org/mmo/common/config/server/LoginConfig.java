package org.mmo.common.config.server;

import com.alibaba.fastjson.JSON;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;

/**
 * 登录服配置
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Component
@ConfigurationProperties("login")
public class LoginConfig {

    /**
     * 服务器id
     */
    private int id;

    /**
     * ip地址
     */
    private String privateIp;

    /**
     * rpc端口
     */
    @Min(1024)
    private int rpcPort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrivateIp() {
        return privateIp;
    }

    public void setPrivateIp(String privateIp) {
        this.privateIp = privateIp;
    }

    public int getRpcPort() {
        return rpcPort;
    }

    public void setRpcPort(int rpcPort) {
        this.rpcPort = rpcPort;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
