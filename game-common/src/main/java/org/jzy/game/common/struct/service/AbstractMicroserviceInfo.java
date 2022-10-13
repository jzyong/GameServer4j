package org.jzy.game.common.struct.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.jzy.game.proto.CommonRpcServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * 微服务抽象
 * 
 * @author jzy
 * @mail 359135103@qq.com
 */
public abstract class AbstractMicroserviceInfo implements IMicroserviceInfo {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMicroserviceInfo.class);
	private ManagedChannel channel;

	/**
	 * 服务器id
	 */
	private String id;
	/**
	 * 连接地址
	 */
	private String url;

	/**
	 * 微服务名称
	 */
	private String name;

	private CommonRpcServiceGrpc.CommonRpcServiceBlockingStub commonRpcServiceBlockingStub;
	private CommonRpcServiceGrpc.CommonRpcServiceStub commonRpcServiceStub;

	public AbstractMicroserviceInfo() {
	}

	public AbstractMicroserviceInfo(String id, String url, String name) {
		this(id, url, name, null);
	}

	public AbstractMicroserviceInfo(String id, String url, String name, Executor executor) {
		this.id = id;
		this.url = url;
		ManagedChannelBuilder<?> builder = ManagedChannelBuilder.forTarget(url);
		channel = builder.usePlaintext().build();
		LOGGER.info("connect to rpc：{}-{}  {}", name, id, url);
	}

	@Override
	public void register() {
		commonRpcServiceBlockingStub = CommonRpcServiceGrpc.newBlockingStub(getChannel());
		commonRpcServiceStub = CommonRpcServiceGrpc.newStub(getChannel());
	}

	public void stop() {
		LOGGER.info("close from micro-service：{}-{} {}", name, id, url);
		if (channel != null) {
			channel.shutdownNow();
		}

	}

	@Override
	public ManagedChannel getChannel() {
		return channel;
	}

	public void setChannel(ManagedChannel channel) {
		this.channel = channel;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CommonRpcServiceGrpc.CommonRpcServiceBlockingStub getCommonRpcServiceBlockingStub() {
		return commonRpcServiceBlockingStub;
	}

	public CommonRpcServiceGrpc.CommonRpcServiceStub getCommonRpcServiceStub() {
		return commonRpcServiceStub;
	}
}
