package org.jzy.game.api.server;

import com.jzy.javalib.network.io.handler.Handler;
import org.jzy.game.api.service.ApiManager;
import org.jzy.game.common.constant.HttpCode;
import org.jzy.game.common.util.RpcHttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 关服
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Handler(path = "/server/reload/config")
public class ReloadConfigHandler extends RpcHttpHandler {
	public static final Logger LOGGER = LoggerFactory.getLogger(ReloadConfigHandler.class);

	@Override
	public void run() {
		String message = "加载配置成功";
		try {
			ApiManager.getInstance().getApiConfigService().loadConfig();
			LOGGER.info("后台请求加载配置");
		} catch (Exception e) {
			message = "服务器内部错误:" + e.getMessage();
			getBuilder().setCode(HttpCode.Error);
			LOGGER.error("加载配置", e);
		} finally {
			getBuilder().setResult(message);
		}

	}
}
