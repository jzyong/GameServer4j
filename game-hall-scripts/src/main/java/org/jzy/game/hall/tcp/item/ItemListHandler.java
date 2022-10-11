package org.jzy.game.hall.tcp.item;

import com.jzy.javalib.base.util.IdUtil;
import com.jzy.javalib.network.io.handler.Handler;
import org.jzy.game.hall.struct.GameHandler;
import org.jzy.game.proto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取道具
 *
 * @author jzy
 */
@Handler(mid = MID.ItemListReq_VALUE, msg = ItemListRequest.class)
public class ItemListHandler extends GameHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(ItemListHandler.class);

    @Override
    public void run() {
        ItemListResponse.Builder builder = ItemListResponse.newBuilder();
        builder.addItem(ItemInfo.newBuilder().setConfigId(1).setCount(22).setId(IdUtil.getId()).build());
        LOGGER.info("请求道具");
        sendInnerMsg(builder.build());
    }
}
