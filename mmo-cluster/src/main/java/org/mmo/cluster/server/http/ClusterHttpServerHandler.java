package org.mmo.cluster.server.http;

import org.mmo.engine.io.netty.http.HttpServerIoHandler;
import org.mmo.engine.io.service.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ClusterHttpServerHandler extends HttpServerIoHandler {

    private static final Logger log = LoggerFactory.getLogger(ClusterHttpServerHandler.class);
    private HttpService service;

    public ClusterHttpServerHandler() {

    }

    protected HttpService getHttpService() {
        return service;
    }

    public void setHttpService(HttpService httpService){
        this.service=service;
    }
}
