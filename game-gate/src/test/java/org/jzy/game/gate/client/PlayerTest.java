package org.jzy.game.gate.client;

import com.jzy.javalib.base.util.ByteUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jzy.game.proto.*;

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
                        case -1 ->{
                            System.out.println("加密字符串："+ ByteUtil.bcdToString(bytes));
                        }
                        case MID
                                .LoginRes_VALUE -> {
                            LoginResponse loginResponse = LoginResponse.parseFrom(bytes);
                            System.out.println(MessageFormat.format("登录返回： {0}", loginResponse.toString()));
                        }
                        case MID.PlayerInfoRes_VALUE -> {
                            PlayerInfoResponse playerInfoResponse = PlayerInfoResponse.parseFrom(bytes);
                            System.out.println(MessageFormat.format("玩家信息： {0}", playerInfoResponse.toString()));
                            //请求 道具列表
                            userClient.sendMsg(MID.ItemListReq, ItemListRequest.newBuilder().build());
                        }
                        case MID.ItemListRes_VALUE -> {
                            ItemListResponse itemListResponse = ItemListResponse.parseFrom(bytes);
                            System.out.println(MessageFormat.format("道具信息： {0}", itemListResponse.toString()));
                        }
                        default -> {
                            System.out.println(MessageFormat.format("消息 {0} 未实现", MID.forNumber(messageId)));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //等待网络建立
        Thread.sleep(1000);
    }

    @After
    public void destroy() throws Exception {
        //暂停 50s
        Thread.sleep(50000);
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
