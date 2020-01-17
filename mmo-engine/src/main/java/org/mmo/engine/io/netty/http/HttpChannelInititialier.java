package org.mmo.engine.io.netty.http;//package org.game.engine.io.netty.http;
//
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.handler.codec.http.HttpRequestDecoder;
//import io.netty.handler.codec.http.HttpResponseEncoder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//@Component
//@Scope("prototype")
//public class HttpChannelInititialier extends ChannelInitializer<SocketChannel> {
//
//    @Autowired
//    private HttpServer httpService;
//
//    @Autowired
//    private HttpServerIoHandler handler;
//
//    public HttpChannelInititialier() {
//    }
//
//
//    @Override
//    protected void initChannel(SocketChannel e) throws Exception {
//    	ChannelPipeline pipeline = e.pipeline();
//    	pipeline.addLast("decoder",new HttpRequestDecoder());
//    	pipeline.addLast("aggregator", new StreamChunkAggregator());
//    	pipeline.addLast("encoder",new HttpResponseEncoder());
//    	pipeline.addLast(handler);
//    }
//}
