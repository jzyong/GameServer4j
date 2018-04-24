package org.mmo.cluster.service;

import org.mmo.common.constant.GameProperties;
import org.mmo.common.constant.GlobalProperties;
import org.mmo.engine.server.ServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 测试服务
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Service
public class TestService {
	
	@Value("${name:unknown}")
	private String name;

	@Autowired
	protected ServerProperties serverProperties;
	@Autowired
	protected GameProperties gameProperties;
	@Autowired
	protected GlobalProperties globalProperties;

	public String getMessage() {
		return getMessage(name);
	}

	public String getMessage(String name) {
		return "Hello " + name + serverProperties.toString() + "\r\n" + gameProperties.toString() + "\r\n"
				+ globalProperties.toString();
	}
}
