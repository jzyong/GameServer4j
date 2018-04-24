package org.mmo.common.constant;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 全局配置
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Component
@ConfigurationProperties // no prefix, find root level values.
public class GlobalProperties {

	/**描述信息*/
	@NotEmpty
	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
