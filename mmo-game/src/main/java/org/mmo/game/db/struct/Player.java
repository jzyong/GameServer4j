package org.mmo.game.db.struct;

import org.mmo.common.struct.object.MapObject;
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
    /**
     * 等级
     */
    private int level;
    /**
     * 经验
     */
    private int exp;


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}
