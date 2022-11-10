package org.mmo.cluster.server.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.mmo.engine.io.netty.http.StreamChunkAggregator;
import org.mmo.engine.io.service.HttpService;
import org.mmo.engine.script.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ClusterHttpChannelInitializer extends ChannelInitializer<SocketChannel> {



    @Autowired
    ScriptService scriptService;

    @Autowired
    HttpService httpService;

    public ClusterHttpChannelInitializer() {
    }


    @Override
    protected void initChannel(SocketChannel e) throws Exception {
    	ChannelPipeline pipeline = e.pipeline();
    	pipeline.addLast("decoder",new HttpRequestDecoder());
    	pipeline.addLast("aggregator", new StreamChunkAggregator());
    	pipeline.addLast("encoder",new HttpResponseEncoder());
        ClusterHttpServerHandler clusterHttpServerHandler = new ClusterHttpServerHandler();
        clusterHttpServerHandler.setScriptService(scriptService);
        clusterHttpServerHandler.setHttpService(httpService);
        pipeline.addLast(clusterHttpServerHandler);
    }
}
