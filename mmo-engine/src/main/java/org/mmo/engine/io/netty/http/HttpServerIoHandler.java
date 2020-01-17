package org.mmo.engine.io.netty.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import org.mmo.engine.io.handler.HttpHandler;
import org.mmo.engine.io.message.MsgUtil;
import org.mmo.engine.io.service.HttpService;
import org.mmo.engine.script.ScriptService;
import org.mmo.engine.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO 待完善
 */
@Component
@Scope("prototype")
public abstract class HttpServerIoHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerIoHandler.class);

    protected abstract HttpService getHttpService();
    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);
    private HttpRequest request;
    private HttpPostRequestDecoder decoder;

    @Autowired
    ScriptService scriptService;
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
        	request = (HttpRequest) msg;
            HttpMethod method = request.method();
            String uri = request.uri();
            if (uri == null || uri.length() == 0) {
                LOGGER.warn("{} 请求地址为空", MsgUtil.getRemoteIpPort(ctx.channel()));
                return;
            }
            try {
                if (HttpMethod.GET.equals(method)) {
                    if (uri.contains("?")) {
                        String param = uri.substring(uri.indexOf("?"));
                        if (param != null && param.length() > 0) {
                            if (msg instanceof DefaultFullHttpRequest) {
                                ((DefaultFullHttpRequest) msg).content().writeBytes(param.getBytes());
                            }
                        }
                    }
                }


                decoder = new HttpPostRequestDecoder(factory, request);
                HttpHandler handler = scriptService.getHttpHandler(uri);
                LOGGER.warn(uri);
                if (handler == null) {
                    LOGGER.warn("{} 请求地址{}处理器未实现", MsgUtil.getRemoteIpPort(ctx.channel()), uri);
                    return;
                }
                if(HttpMethod.POST.equals(method)) {
                	Map<String, Object> param = getPostParamsFromChannel((FullHttpRequest) msg);
                	handler.setParam(param);
                }

                long time = TimeUtil.currentTimeMillis();
                handler.setMessage(msg);
                handler.setChannel(ctx.channel());
                handler.setCreateTime(TimeUtil.currentTimeMillis());
        //      xecute(handler);  Executor executor =
        // getHttpService().getExecutor("io");
        ////                executor.execute(handler);

                //TODO 待完善，分配指定线程
                handler.run();

        time = TimeUtil.currentTimeMillis() - time;
                if (time > 20) {
                    LOGGER.warn("{}处理时间超过{}", handler.getClass().getSimpleName(), time);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else  if (msg instanceof HttpContent) {
        	if (decoder != null ) {
                HttpContent chunk = (HttpContent) msg;
                try{
                    decoder.offer(chunk);
                } catch (ErrorDataDecoderException e1) {
                    ctx.channel().close();
                    return;
                }
                if (chunk instanceof LastHttpContent) {
                }
            } else {
                ctx.channel().close();
                return;
            }
         }
    }
    private Map<String, Object> getPostParamsFromChannel(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (fullHttpRequest.method() == HttpMethod.POST) {
            String strContentType = fullHttpRequest.headers().get("Content-type").trim();
            if (strContentType.contains("form")) {
                params = getFormParams(fullHttpRequest);
            } else {
            	params = getFormParams(fullHttpRequest);
            }
            return params;
        }
        return null;
    }
    
    private Map<String, Object> getFormParams(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<String, Object>();
        // HttpPostMultipartRequestDecoder
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fullHttpRequest);
        List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
        for (InterfaceHttpData data : postData) {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MemoryAttribute attribute = (MemoryAttribute) data;
                params.put(attribute.getName(), attribute.getValue());
            }else if(data.getHttpDataType()  == InterfaceHttpData.HttpDataType.FileUpload) {
            	params.put(data.getName(), (FileUpload) data);
            }
        }
        return params;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    public ScriptService getScriptService() {
        return scriptService;
    }

    public void setScriptService(ScriptService scriptService) {
        this.scriptService = scriptService;
    }


}
