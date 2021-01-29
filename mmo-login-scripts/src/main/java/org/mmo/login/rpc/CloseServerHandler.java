package org.mmo.login.rpc;

import com.alibaba.fastjson.JSONObject;
import org.mmo.common.util.RpcHttpHandler;
import org.mmo.engine.io.handler.Handler;
import org.mmo.login.service.LoginManager;
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
        JSONObject jsonParams = getJsonParams();
        LOGGER.info("服务器关闭....{}",jsonParams.getString("test"));
        getBuilder().setResult("关服成功");

        //延迟关服
        LoginManager.getInstance().getLoginService().schedule(()->Runtime.getRuntime().exit(0),5000, TimeUnit.MILLISECONDS);
    }
}
