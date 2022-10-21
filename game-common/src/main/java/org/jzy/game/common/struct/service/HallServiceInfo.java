package org.jzy.game.common.struct.service;

import org.jzy.game.proto.AccountServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Api服务
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
public class HallServiceInfo extends AbstractMicroserviceInfo {
    public static final Logger LOGGER = LoggerFactory.getLogger(HallServiceInfo.class);




    public HallServiceInfo(String id, String url, String name) {
        super(id, url, name);
    }

    @Override
    public void register() {
        super.register();
    }

}
