package org.mmo.engine.io.service;

import org.springframework.stereotype.Service;

/**
 * http 通信
 *
 */
@Service
public abstract class HttpService implements INettyService<String> {

//    private static final Logger LOGGER = LoggerFactory.getLogger(HttpService.class);


    public HttpService() {
    }
    





    @Override
    public boolean sendMsg(Object msg) {
        throw new UnsupportedOperationException();
    }

}
