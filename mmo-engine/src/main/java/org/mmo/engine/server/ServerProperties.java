package org.mmo.engine.server;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

/**
 * 服务器配置信息
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Component
@ConfigurationProperties("server") // 前缀 如server.id;
public class ServerProperties {

	/** 服务器id 六位数 十万位：服务器类型 个位数服务器编号 */
	@Min(100000)
	@Max(999999)
	private int id;

	/** 服务器版本号 每两位一个区间 */
	@Min(100000)
	@Max(999999)
	private int version;

	/** 需要连接的其他服务器 */
	private List<ConnectionServer> connections = new ArrayList<>();

	/** 网络地址 */
	private Address address;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<ConnectionServer> getConnections() {
		return connections;
	}

	public void setConnections(List<ConnectionServer> connections) {
		this.connections = connections;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	/**
	 * 需要连接的服务器
	 * 
	 * @author JiangZhiYong
	 * @mail 359135103@qq.com
	 */
	public static class ConnectionServer {
		@NotEmpty
		private String host;
		private int port;

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		@Override
		public String toString() {
			return JSON.toJSONString(this);
		}
	}

	/**
	 * 网络地址
	 * 
	 * @author JiangZhiYong
	 * @mail 359135103@qq.com
	 */
	public static class Address {
		@NotEmpty
		private String host;
		/** 网关需要开多个端口 */
		private List<Integer> ports;
		private int httpPort;

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public List<Integer> getPorts() {
			return ports;
		}

		public void setPorts(List<Integer> ports) {
			this.ports = ports;
		}

		public int getHttpPort() {
			return httpPort;
		}

		public void setHttpPort(int httpPort) {
			this.httpPort = httpPort;
		}

		@Override
		public String toString() {
			return JSON.toJSONString(this);
		}
	}

}
