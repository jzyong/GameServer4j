package org.mmo.engine.thread;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 线程配置
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Component
@ConfigurationProperties("executor")
public class ExecutorProperties {

    // 服务器线程池core大小
    private int corePoolSize = 1;

    // 服务器线程池max大小
    private int maximumPoolSize = 5;

    // 服务器线程池保持时间秒
    private int keepAliveTime = 300;

    //名称
    private String name="IO";

    // 服务器线程池任务数
    private int maxCommandSize = 512 * 512;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxCommandSize() {
        return maxCommandSize;
    }

    public void setMaxCommandSize(int maxCommandSize) {
        this.maxCommandSize = maxCommandSize;
    }
}
