package org.jzy.game.common.config.server;

import com.alibaba.fastjson.JSON;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 游戏服配置
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Component
@ConfigurationProperties("hall")
public class HallConfig {

    /**
     * 服务器id
     */
    @Max(1000)
    @Min(1)
    private int id;

    /**
     * ip地址
     */
    private String privateIp;

    /**
     * rpc端口
     */
    private int rpcPort;


    /**
     * 最大等级
     */
    private int maxLevel;


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

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getRpcPort() {
        return rpcPort;
    }

    public void setRpcPort(int rpcPort) {
        this.rpcPort = rpcPort;
    }

    public String buildRpcUrl(){
        return this.privateIp+":"+this.rpcPort;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
