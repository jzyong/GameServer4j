package org.mmo.engine.io.handler;

import com.google.protobuf.Message;
import io.grpc.stub.StreamObserver;

/**
 * Rpc 请求
 * <p>实现此类主要是为了rpc调用能动态添加代码逻辑</p>
 * @author jzy
 * @mail 359135103@qq.com
 */
public abstract class RpcHandler<Req extends Message,Res extends Message> implements IHandler {

    /**
     * 唯一id|角色id
     */
    private long id;

    /**
     * 请求消息
     */
    private Req request;

    /**
     * 返回消息
     */
    private Res response;
    /**
     *
     */
    private StreamObserver<Res> responseObserver;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Req getRequest() {
        return request;
    }

    public void setRequest(Req request) {
        this.request = request;
    }

    public Res getResponse() {
        return response;
    }

    public void setResponse(Res response) {
        this.response = response;
    }

    public StreamObserver<Res> getResponseObserver() {
        return responseObserver;
    }

    public void setResponseObserver(StreamObserver<Res> responseObserver) {
        this.responseObserver = responseObserver;
    }

    /**
     * 发送消息
     */
    public void sendMsg(){
        responseObserver.onNext(getResponse());
        responseObserver.onCompleted();
    }
}
