package org.mmo.common.struct.object;

import org.springframework.data.mongodb.core.mapping.Document;

import com.alibaba.fastjson.JSON;

/**
 * 玩家数据
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Document(collection = "player")
public class Player extends MapObject {

	public String toString() {
		return JSON.toJSONString(this);
	}
}
