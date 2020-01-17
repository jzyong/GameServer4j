package org.mmo.engine.io.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP消息
 * //TODO 待优化
 */
public abstract class HttpHandler implements IHandler {

    protected DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);

    protected Channel channel;    // 消息来源
    protected DefaultFullHttpRequest message;      // 请求消息
    protected int content;      // 对应http:/127.0.0.1:8080/login 中的login
    protected long createTime;
    protected Map<String, Object> param = new HashMap<>();

    @Override
    public Object clone()
            throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public DefaultFullHttpRequest getMessage() { // HttpRequestImpl
        return this.message;
    }

    @Override
    public void setMessage(Object message) {
        if (message instanceof DefaultFullHttpRequest) {
            this.message = (DefaultFullHttpRequest) message;
        }
    }

    public DefaultFullHttpResponse getResponse() {
		return response;
	}

	public void setResponse(DefaultFullHttpResponse response) {
		this.response = response;
	}

	@Override
    public long getCreateTime() {
        return this.createTime;
    }

    @Override
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void response() {
    	if(channel!=null && channel.isActive()) {
    		if(response.status() == HttpResponseStatus.OK) {
    			response.headers().set(HttpHeaderNames.CONTENT_LENGTH,
		    	response.content().readableBytes());
		    	response.headers().set(HttpHeaderNames.ACCEPT_CHARSET,"UTF-8");
		    	response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain; charset=utf-8");
    		}
    		channel.writeAndFlush(response);
    	}else {
    		System.out.println("HttpHandler {channel 未激活！}");
    	}
    }

    @Override
    public byte[] getMsgBytes() {
        return response.content().array();
    }

    @Override
    public void setMsgBytes(byte[] msgBytes) {
    	response.content().writeBytes(msgBytes);
    }
    
    public byte[] getRequestContentBytes() {
    	ByteBuf buf = message.content();
    	return buf.array();
    }

	public Map<String, Object> getParam() {
		return param;
	}

	public void setParam(Map<String, Object> param) {
		this.param = param;
	}
}
