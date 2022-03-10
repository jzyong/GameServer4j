package org.jzy.game.common.constant;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 全局配置
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Component
@ConfigurationProperties(prefix = "global") // no prefix, find root level values.
public class GlobalProperties {

	/**描述信息*/
	@NotEmpty
	private String info;

	/**
	 * zookeeper地址
	 */
	private String zookeeperUrl;

	/**
	 * 本地个性化设置
	 */
	private String profile;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getZookeeperUrl() {
		return zookeeperUrl;
	}

	public void setZookeeperUrl(String zookeeperUrl) {
		this.zookeeperUrl = zookeeperUrl;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
}
