package org.jzy.game.common.struct.service;

import io.grpc.ManagedChannel;

/**
 * 微服务接口
 * 
 * @author jzy
 * @mail 359135103@qq.com
 */
public interface IMicroserviceInfo {

	/**
	 * 注册微服务
	 */
	void register();

	/**
	 * 获取连接对象
	 */
	ManagedChannel getChannel();

	/**
	 * 微服务id
	 * 
	 * @return
	 */
	String getId();

	/**
	 * 微服务rpc地址
	 * 
	 * @return
	 */
	String getUrl();

	/**
	 * 服务名称
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 停止微服务
	 */
	void stop();

}
