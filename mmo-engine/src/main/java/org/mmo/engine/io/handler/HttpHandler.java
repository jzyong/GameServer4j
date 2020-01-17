package org.mmo.engine.io.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP消息
 */
public abstract class HttpHandler implements IHandler {
    private static final Logger LOGGER= LoggerFactory.getLogger(HttpHandler.class);

    protected DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);

    protected Channel channel;    // 消息来源
    protected DefaultFullHttpRequest message;      // 请求消息
    //创建时间
    protected long createTime;
    //参数
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

    /**
     * 返回消息
     */
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
    		LOGGER.warn("HttpHandler {channel 未激活！} {}",response.toString());
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
