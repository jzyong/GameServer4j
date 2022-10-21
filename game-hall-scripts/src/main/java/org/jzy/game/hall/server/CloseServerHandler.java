package org.jzy.game.hall.server;

import com.jzy.javalib.network.io.handler.Handler;
import org.jzy.game.common.util.RpcHttpHandler;
import org.jzy.game.hall.service.HallManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 关服
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Handler(path = "/server/gm/close")
public class CloseServerHandler extends RpcHttpHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(CloseServerHandler.class);

    @Override
    public void run() {
        getBuilder().setResult("关服成功");

        //延迟关服
        HallManager.getInstance().getHallService().schedule(() -> Runtime.getRuntime().exit(0), 1000, TimeUnit.MILLISECONDS);
    }
}
