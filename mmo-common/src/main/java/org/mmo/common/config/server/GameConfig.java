package org.mmo.common.config.server;

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
@ConfigurationProperties("game")
public class GameConfig {

    /**
     * 服务器id
     */
    private int id;

    /**
     * ip地址
     */
    private String privateIp;


    /**
     * 最大等级
     */
    @Max(100)
    @Min(1)
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
