package org.mmo.gate.tcp.account;

import org.mmo.engine.io.handler.Handler;
import org.mmo.engine.io.handler.TcpHandler;
import org.mmo.message.LoginRequest;
import org.mmo.message.MIDMessage;

/**
 * 登录
 * <p>登录因游戏类型需要修改</p>
 * @author jzy
 */
@Handler(mid = MIDMessage.MID.LoginReq_VALUE,msg = LoginRequest.class)
public class LoginHandler extends TcpHandler {
    @Override
    public void run() {

        var request=(LoginRequest)getMsg();
        //TODO 获取登录服rpc
    }
}
