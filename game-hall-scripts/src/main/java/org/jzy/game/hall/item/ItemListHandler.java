package org.jzy.game.hall.item;

import com.jzy.javalib.base.util.IdUtil;
import com.jzy.javalib.network.io.handler.Handler;
import org.jzy.game.hall.struct.HallHandler;
import org.jzy.game.proto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取道具
 *
 * @author jzy
 */
@Handler(mid = MID.ItemListReq_VALUE, msg = ItemListRequest.class)
public class ItemListHandler extends HallHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(ItemListHandler.class);

    @Override
    public void run() {
        ItemListResponse.Builder builder = ItemListResponse.newBuilder();
        builder.addItem(ItemInfo.newBuilder().setConfigId(1).setCount(22).setId(IdUtil.getId()).build());
        LOGGER.info("请求道具");
        sendInnerMsg(builder.build());
    }
}
