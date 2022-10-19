package org.jzy.game.api.server;

import com.jzy.javalib.base.script.ScriptManager;
import com.jzy.javalib.base.util.StringUtil;
import com.jzy.javalib.network.io.handler.Handler;
import org.jzy.game.common.util.RpcHttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 关服
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Handler(path = "/server/reload/script")
public class ReloadScriptHandler extends RpcHttpHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(ReloadScriptHandler.class);

    @Override
    public void run() {

        // 可从本地替换源文件加载，或从数据库加载源文件加载
        var result= ScriptManager.getInstance().loadJava(null);

        getBuilder().setResult("加载脚本成功：" + result);
        LOGGER.info("后台请求加载脚本:{}", result);

    }
}
