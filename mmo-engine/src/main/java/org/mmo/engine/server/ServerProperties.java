//package org.mmo.engine.server;
//
//import com.alibaba.fastjson.JSON;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.stereotype.Component;
//
//import javax.validation.constraints.Max;
//import javax.validation.constraints.Min;
//import javax.validation.constraints.NotEmpty;
//import java.util.List;
//
///**
// * 服务器配置信息
// *
// * @author JiangZhiYong
// * @mail 359135103@qq.com
// */
//@Deprecated
//@Component
//@ConfigurationProperties("server") // 前缀 如server.id;
//public class ServerProperties {
//
//	/** 服务器id 六位数 十万位：服务器类型 个位数服务器编号 */
//	@Min(100000)
//	@Max(999999)
//	private int id;
//
//	/** 服务器版本号 每两位一个区间 */
//	@Min(100000)
//	@Max(999999)
//	private int version;
//
//
//	/**服务器名字*/
//	private String name;
//
//	/**
//	 * 本地ip地址
//	 */
//	@Deprecated
//	private String ip;
//	/**
//	 * 外网ip地址
//	 */
//	@Deprecated
//	private String wwwip;
//
//	/**
//	 * 服务器注册心跳
//	 */
//	public static final int ServerRegisterHeart=2;
//
//
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//
//
//	public int getVersion() {
//		return version;
//	}
//
//	public void setVersion(int version) {
//		this.version = version;
//	}
//
//
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	@Override
//	public String toString() {
//		return JSON.toJSONString(this);
//	}
//
//
//	public String getIp() {
//		return ip;
//	}
//
//	public void setIp(String ip) {
//		this.ip = ip;
//	}
//
//	public String getWwwip() {
//		return wwwip;
//	}
//
//	public void setWwwip(String wwwip) {
//		this.wwwip = wwwip;
//	}
//}
