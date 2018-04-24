package org.mmo.common.constant;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

/**
 * 游戏逻辑服配置 <br>
 * 前缀 game
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Component
@ConfigurationProperties("game")
public class GameProperties {

	/** 最大等级 */
	@Max(100)
	@Min(1)
	private int maxLevel;

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
