package org.jzy.game.api.db.config.enity;

import com.alibaba.fastjson.JSONObject;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 商城道具
 *
 * @author jzy
 */
@Document(collection = "config_shop_item")
public class ShopItemCfg {

    @Id
    private ObjectId _id;

    private int id;

    /**
     * 价格
     */
    private int price;

    /**
     * 物品
     */
    private String items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
