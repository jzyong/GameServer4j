package org.mmo.engine.io.netty.config;

/**
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public class NettyServerConfig {

    /**名称*/
    private String name="game";

    /**接收数据缓冲大小*/
    protected int receiveBufferSize = 1048576;

    /**发送数据缓冲大小*/
    protected int sendBufferSize = 1048576;

    // Tcp没有延迟
    private boolean tcpNoDelay = true;
    // 读取空闲时间检测
    private int readerIdleTime = 120;

    // 写入空闲时间检测
    private int writerIdleTime = 120;

    //连接超时
    private int connectTimeOut=5000;

    // 是否重用地址
    private boolean reuseAddress = true;

    /**端口*/
    private int port=8000;

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

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
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

    public boolean isReuseAddress() {
        return reuseAddress;
    }

    public void setReuseAddress(boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
