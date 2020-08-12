package org.mmo.gate.tcp.account;

import org.mmo.engine.io.handler.Handler;
import org.mmo.engine.io.handler.TcpHandler;
import org.mmo.engine.util.TimeUtil;
import org.mmo.message.HeartRequest;
import org.mmo.message.HeartResponse;
import org.mmo.message.MIDMessage;

/**
 * 心跳
 *
 * @author jzy
 */
@Handler(mid = MIDMessage.MID.HeartReq_VALUE, msg = HeartRequest.class)
public class HeartHandler extends TcpHandler {
    @Override
    public void run() {
        sendMsg(HeartResponse.newBuilder().setTime(TimeUtil.currentTimeMillis()).build());
    }
}
