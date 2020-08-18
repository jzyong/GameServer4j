package org.mmo.game.tcp.item;

import org.mmo.engine.io.handler.Handler;
import org.mmo.engine.io.handler.TcpHandler;
import org.mmo.engine.util.IdUtil;
import org.mmo.game.struct.GameHandler;
import org.mmo.message.ItemInfo;
import org.mmo.message.ItemListRequest;
import org.mmo.message.ItemListResponse;
import org.mmo.message.MIDMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取道具
 *
 * @author jzy
 */
@Handler(mid = MIDMessage.MID.ItemListReq_VALUE, msg = ItemListRequest.class)
public class ItemListHandler extends GameHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(ItemListHandler.class);

    @Override
    public void run() {
        ItemListResponse.Builder builder = ItemListResponse.newBuilder();
        builder.addItem(ItemInfo.newBuilder().setConfigId(1).setCount(22).setId(IdUtil.getId()).build());
        LOGGER.info("请求道具");
        sendInnerMsg(MIDMessage.MID.ItemListRes_VALUE, builder.build());
    }
}
