package org.mmo.game.server.client;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * 登陆服tcp channel初始化
 * 
 * @author JiangZhiYong
 * @date 2018/12/11
 */
public class GameToGateChannelInitializer extends ChannelInitializer<SocketChannel> {


    public GameToGateChannelInitializer() {

    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("Codec", new GameToGateMessageCodec());
        ch.pipeline().addLast("MessageHandler", new GameToGateClientHandler());
    }
}