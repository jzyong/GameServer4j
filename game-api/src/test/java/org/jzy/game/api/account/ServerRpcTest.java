package org.jzy.game.api.account;

import com.alibaba.fastjson.JSONObject;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.Before;
import org.junit.Test;
import org.jzy.game.proto.*;

import java.text.MessageFormat;

/**
 * 服务器 rpc
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
public class ServerRpcTest {

    ManagedChannel channel;
    CommonRpcServiceGrpc.CommonRpcServiceStub commonRpcServiceStub;
    CommonRpcServiceGrpc.CommonRpcServiceBlockingStub commonRpcServiceBlockingStub;

    @Before
    public void init() {
        channel = ManagedChannelBuilder.forTarget("127.0.0.1:7000").usePlaintext().build();
        commonRpcServiceStub = CommonRpcServiceGrpc.newStub(channel);
        commonRpcServiceBlockingStub = CommonRpcServiceGrpc.newBlockingStub(channel);
    }

    /**
     * 关闭服务器
     */
    @Test
    public void testCloseServer() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("test", "closeServer");
        HttpRequest request = HttpRequest.newBuilder().setPath("/server/gm/close").setJsonParam(jsonObject.toJSONString()).build();

        var httpResponse = commonRpcServiceBlockingStub.httpPost(request);
        System.out.println(MessageFormat.format("返回：{0}", httpResponse));
    }

    /**
     * 加载脚本
     */
    @Test
    public void testReloadConfig() {
        HttpRequest request = HttpRequest.newBuilder().setPath("/server/reload/config").build();
        var httpResponse = commonRpcServiceBlockingStub.httpPost(request);
        System.out.println(MessageFormat.format("返回：{0}", httpResponse));
    }

    /**
     * 加载脚本
     */
    @Test
    public void testReloadScript() {
        HttpRequest request = HttpRequest.newBuilder().setPath("/server/reload/script").build();
        var httpResponse = commonRpcServiceBlockingStub.httpPost(request);
        System.out.println(MessageFormat.format("返回：{0}", httpResponse));
    }

}
