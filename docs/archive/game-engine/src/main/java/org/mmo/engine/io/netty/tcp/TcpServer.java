package org.mmo.engine.io.netty.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.mmo.engine.io.netty.config.NettyServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class TcpServer {

    private static Logger LOGGER = LoggerFactory.getLogger(TcpServer.class);
    private ChannelFuture channelFuture;
    private NettyServerConfig nettyServerConfig;
    protected boolean isRunning = false;
    private ChannelInitializer<SocketChannel> channelInitializer;

    public TcpServer( ) {
    }


    public void start() {
        synchronized (this) {
            if (!isRunning) {
                isRunning = true;
                startServer();
            }
        }
    }

    public void stop() {
        synchronized (this) {
            if (!isRunning) {
                LOGGER.info("Server " + "服务器" + "is already stoped.");
                return;
            }
            isRunning = false;
            try {
                channelFuture.channel().closeFuture();
                LOGGER.info("Server is stoped.");
            } catch (Exception ex) {
                LOGGER.error("", ex);
            }
        }
    }

    public void startServer() {
        new Thread() {
            @Override
            public void run() {
                EventLoopGroup bossGroup = new NioEventLoopGroup();
                EventLoopGroup workGroup = new NioEventLoopGroup();
                try {
                    ServerBootstrap boot = new ServerBootstrap();
                    boot.group(bossGroup, workGroup);
                    boot.channel(NioServerSocketChannel.class);
                    boot.option(ChannelOption.TCP_NODELAY, nettyServerConfig.isTcpNoDelay());
                    boot.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, nettyServerConfig.getConnectTimeOut());
                    boot.option(ChannelOption.SO_KEEPALIVE, true);
                    boot.option(ChannelOption.SO_REUSEADDR, nettyServerConfig.isReuseAddress());
                    boot.option(ChannelOption.SO_RCVBUF, nettyServerConfig.getReceiveBufferSize());
                    boot.option(ChannelOption.SO_SNDBUF, nettyServerConfig.getSendBufferSize());
                    boot.childHandler(channelInitializer);
                    int port = nettyServerConfig.getPort();
                    channelFuture = boot.bind(port).sync();
                    channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                        @Override
                        public void operationComplete(Future<? super Void> future) throws Exception {
                            if(future.isSuccess()) {
                                LOGGER.info("TCP服务 {} 端口：{} 启动完成", nettyServerConfig.getName(), nettyServerConfig.getPort());
                            }else {
                                LOGGER.info("TCP服务 {} 端口：{} 启动失败", nettyServerConfig.getName(), nettyServerConfig.getPort());
                            }
                        }
                    });
                    Channel channel = channelFuture.channel();
                    channel.closeFuture().sync();

                } catch (InterruptedException e) {
                    LOGGER.error("", e);
                } finally {
                    bossGroup.shutdownGracefully();
                    workGroup.shutdownGracefully();
                }
            }
        }.start();
    }

    public ChannelInitializer<SocketChannel> getChannelInitializer() {
        return channelInitializer;
    }

    public void setChannelInitializer(ChannelInitializer<SocketChannel> channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    public NettyServerConfig getNettyServerConfig() {
        return nettyServerConfig;
    }

    public void setNettyServerConfig(NettyServerConfig nettyServerConfig) {
        this.nettyServerConfig = nettyServerConfig;
    }
}
