package org.jzy.game.common.config.server;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;

/**
 * 后台配置
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Component
@ConfigurationProperties("manage")
public class ManageConfig {

    /**
     * 服务器id
     */
    private int id;

    /**
     * http端口
     */
    @Min(1024)
    private int httpPort;

    /**
     * ip地址
     */
    private String publicIp;

    /**
     * kafka URL
     */
    private String kafkaUrl;

    /**
     * 提取kafka日志
     */
    private boolean fetchKafkaLog;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public String getKafkaUrl() {
        return kafkaUrl;
    }

    public void setKafkaUrl(String kafkaUrl) {
        this.kafkaUrl = kafkaUrl;
    }

    public boolean isFetchKafkaLog() {
        return fetchKafkaLog;
    }

    public void setFetchKafkaLog(boolean fetchKafkaLog) {
        this.fetchKafkaLog = fetchKafkaLog;
    }
}
