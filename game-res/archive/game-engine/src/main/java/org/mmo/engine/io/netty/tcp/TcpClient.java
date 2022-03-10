package org.mmo.engine.io.netty.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.mmo.engine.io.netty.config.NettyClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Component
@Scope("prototype")
public class TcpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpClient.class);

    Bootstrap boot;
    NioEventLoopGroup workGroup;

    private NettyClientConfig nettyClientConfig;
    public static final AttributeKey<Object> ChannelParamsKey = AttributeKey.valueOf("channelParamsKey");

    protected boolean isRunning = false;
    private String host = "";
    private int port;
    private ChannelInitializer<SocketChannel> channelInitializer;

    private Channel channel;

    public TcpClient( ) {
    }



    @PostConstruct
    public void init() {
        boot = new Bootstrap();
        workGroup = new NioEventLoopGroup();
        boot.group(workGroup);
        boot.channel(NioSocketChannel.class);
        boot.option(ChannelOption.TCP_NODELAY, true);
    }

    /**
     * 启动
     */
    public void start(){
        if(nettyClientConfig==null){
            throw new IllegalStateException(String.format("nettyClientConfig 未赋值"));
        }
        if(channelInitializer==null){
            throw new IllegalStateException(String.format("channelInitializer 未赋值"));
        }

        boot.option(ChannelOption.SO_RCVBUF, nettyClientConfig.getReceiveBufferSize());
        boot.option(ChannelOption.SO_SNDBUF, nettyClientConfig.getSendBufferSize());
        boot.handler(channelInitializer);
        connect(nettyClientConfig);
    }


    public void stop() {
        try {
            LOGGER.info("tcpClient到主机{}端口{}的连接【关闭成功】.", host, port);
        } catch (Exception ex) {
            LOGGER.error("", ex);
        } finally {
            boot.connect().channel().close();
        }
    }


    private void connect(NettyClientConfig nettyClientConfig) {
        new Thread(() -> {
            for (int i = 0, size = nettyClientConfig.getConnectCount() ; i < size; i++) {
                try {
                    host = nettyClientConfig.getIp();
                    port = nettyClientConfig.getPort();
                    // LOGGER.warn("初始化到服务器的连接：" + value.toString());
                    boot.remoteAddress(new InetSocketAddress(host, port));
                    ChannelFuture channelFuture = boot.connect(host, port);
                    channelFuture.await(10000L, TimeUnit.MILLISECONDS);
                    channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                        @Override
                        public void operationComplete(Future<? super Void> future) throws Exception {
                            if (future.isSuccess()) {
                                Channel c = channelFuture.channel();
                                if (c != null) {
                                    LOGGER.info("成功连接到服务器{}:{}" ,host,port);
                                    TcpClient.this.setChannel(c);
                                    if(nettyClientConfig.getChannelParam()!=null){
                                        Attribute<Object> attr = channel.attr(ChannelParamsKey);
                                        attr.set(nettyClientConfig.getChannelParam());
                                    }
                                }
                            } else {
                                LOGGER.warn("失败！连接到服务器：" + nettyClientConfig.toString());
                            }
                        }
                    });
                    channelFuture.channel().closeFuture().sync();
                } catch (Exception ex) {
                    LOGGER.error("tcpClient 137错误-------------", ex);
                }
            }
        }).start();

    }


    public NettyClientConfig getNettyClientConfig() {
        return nettyClientConfig;
    }

    public void setNettyClientConfig(NettyClientConfig nettyClientConfig) {
        this.nettyClientConfig = nettyClientConfig;
    }

    public ChannelInitializer<SocketChannel> getChannelInitializer() {
        return channelInitializer;
    }

    public void setChannelInitializer(ChannelInitializer<SocketChannel> channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
