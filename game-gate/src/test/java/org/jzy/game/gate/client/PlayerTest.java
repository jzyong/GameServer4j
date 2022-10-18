package org.jzy.game.gate.client;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jzy.game.proto.LoginRequest;
import org.jzy.game.proto.LoginResponse;
import org.jzy.game.proto.MID;

import java.text.MessageFormat;

/**
 * 测试玩家
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
public class PlayerTest {

    private UserClient userClient;

    @Before
    public void init() throws Exception {
        userClient = new UserClient("127.0.0.1", 7020);
        userClient.setMessageDistribute(new UserClient.IMessageDistribute() {
            @Override
            public void handMessage(int messageId, byte[] bytes) {
                try {
                    switch (messageId) {
                        case MID
                                .LoginRes_VALUE -> {
                            LoginResponse loginResponse = LoginResponse.parseFrom(bytes);
                            System.out.println(MessageFormat.format("登录返回： {}", loginResponse.toString()));
                        }
                        default -> {
                            System.out.println(MessageFormat.format("消息 {} 未实现", MID.forNumber(messageId)));
                        }
                    }
                } catch (Exception e) {
                    System.out.println(MessageFormat.format("消息处理异常 {} ", e));
                }
            }
        });
        //等待网络建立
        Thread.sleep(1000);
    }

    @After
    public void destroy() throws Exception {
        //暂停 5s
        Thread.sleep(5000);
    }

    /**
     * 测试用户登录
     */
    @Test
    public void testUserLogin() {
        LoginRequest.Builder builder = LoginRequest.newBuilder();
        builder.setPassword("111");
        builder.setAccount("test1");
        userClient.sendMsg(MID.LoginReq, builder.build());
    }

}
