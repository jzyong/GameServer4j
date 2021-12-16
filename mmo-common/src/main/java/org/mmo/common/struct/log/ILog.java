package org.mmo.common.struct.log;

import com.alibaba.fastjson.JSON;

/**
 * 日志
 * @author jzy
 * @mail 359135103@qq.com
 */
public interface ILog {

    long getId();

    String topic();

    default String key() {
        return String.valueOf(getId());
    }

    default String value() {
        return JSON.toJSONString(this);
    }

}
