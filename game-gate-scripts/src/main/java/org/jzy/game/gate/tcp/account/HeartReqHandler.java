package org.jzy.game.gate.tcp.account;

import com.jzy.javalib.base.util.TimeUtil;
import com.jzy.javalib.network.io.handler.Handler;
import com.jzy.javalib.network.io.handler.TcpHandler;
import org.jzy.game.proto.HeartRequest;
import org.jzy.game.proto.HeartResponse;
import org.jzy.game.proto.MessageId;

/**
 * 心跳
 *
 * @author jzy
 */
@Handler(mid = MessageId.MID.HeartReq_VALUE, msg = HeartRequest.class)
public class HeartReqHandler extends TcpHandler {
    @Override
    public void run() {
        sendClientMsg(HeartResponse.newBuilder().setTime(TimeUtil.currentTimeMillis()).build());
    }
}
