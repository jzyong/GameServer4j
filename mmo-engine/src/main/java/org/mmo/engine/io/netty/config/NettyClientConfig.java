package org.mmo.engine.io.netty.config;

/**
 * 客户端连接配置
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public class NettyClientConfig {

    /**名称*/
    private String name="game";

    /**接收数据缓冲大小*/
    protected int receiveBufferSize = 1048576;

    /**发送数据缓冲大小*/
    protected int sendBufferSize = 1048576;

    // 读取空闲时间检测
    private int readerIdleTime = 120;

    // 写入空闲时间检测
    private int writerIdleTime = 120;

    //连接超时
    private int connectTimeOut=5000;

    /**ip地址*/
    private String ip="127.0.0.1";

    /**端口*/
    private int port=8000;

    /**连接数*/
    private int connectCount=1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReceiveBufferSize() {
        return receiveBufferSize;
    }

    public void setReceiveBufferSize(int receiveBufferSize) {
        this.receiveBufferSize = receiveBufferSize;
    }

    public int getSendBufferSize() {
        return sendBufferSize;
    }

    public void setSendBufferSize(int sendBufferSize) {
        this.sendBufferSize = sendBufferSize;
    }


    public int getReaderIdleTime() {
        return readerIdleTime;
    }

    public void setReaderIdleTime(int readerIdleTime) {
        this.readerIdleTime = readerIdleTime;
    }

    public int getWriterIdleTime() {
        return writerIdleTime;
    }

    public void setWriterIdleTime(int writerIdleTime) {
        this.writerIdleTime = writerIdleTime;
    }

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getConnectCount() {
        return connectCount;
    }

    public void setConnectCount(int connectCount) {
        this.connectCount = connectCount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return String.format("%s:%d",ip,port);
    }
}
