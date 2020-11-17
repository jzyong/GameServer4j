package org.mmo.engine.io.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * rpc 配置
 */
@Deprecated
@Component
@ConfigurationProperties("rpc")
public class RpcProperties {

    /**服务器本地启动端口号*/
    private int serverPort=1234;
    /**连接远端服务器地址*/
    private String serverURL="localhost:1234";


    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }
}
