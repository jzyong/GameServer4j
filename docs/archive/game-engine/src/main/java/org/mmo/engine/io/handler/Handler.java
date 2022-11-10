package org.mmo.engine.io.handler;

import com.google.protobuf.Message;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 消息处理器注册
 * @author JiangZhiYong
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Handler {
	/**
	 * TCP 消息id
	 */
	int mid() default 0;

	/***
	 * http请求路径
	 * 
	 * @return
	 */
	String path() default "";

	/**
	 * 
	 * @return 描述
	 */
	String desc() default "";
	
	/**
	 * 调用的线程
	 * 
	 * @return
	 */
	String executor() default "io";

	/**
	 * tcp 请求的消息类
	 * 
	 * @return
	 */
	Class<? extends Message> msg() default Message.class;
}
