package org.jzy.game.common.util;

import com.alibaba.fastjson.JSONObject;
import com.jzy.javalib.base.util.StringUtil;
import com.jzy.javalib.network.io.handler.RpcHandler;
import org.jzy.game.proto.HttpRequest;
import org.jzy.game.proto.HttpResponse;

/**
 * rpc适配http调用类
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
public abstract class RpcHttpHandler extends RpcHandler<HttpRequest, HttpResponse> {

    private HttpResponse.Builder builder;

    /**
     * 构建返回消息
     *
     * @return
     */
    public HttpResponse.Builder getBuilder() {
        if (builder == null) {
            builder = HttpResponse.newBuilder().setCode(200);
        }
        return builder;
    }

    @Override
    public HttpResponse getResponse() {
        return builder.build();
    }

    public JSONObject getJsonParams() {
        String jsonParam = getRequest().getJsonParam();
        if (StringUtil.isEmpty(jsonParam)) {
            return null;
        }
        return JSONObject.parseObject(jsonParam);
    }

}
