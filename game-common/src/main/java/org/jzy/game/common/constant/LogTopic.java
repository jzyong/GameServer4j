package org.jzy.game.common.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志话题
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
public enum LogTopic {

    /**
     * 登录
     */
    Login("LoginLog");


    private String name;

    LogTopic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * 返回字符串列表
     *
     * @return
     */
    public static List<String> toNameList() {
        List<String> list = new ArrayList<>();
        for (LogTopic logTopic : LogTopic.values()) {
            list.add(logTopic.getName());
        }
        return list;
    }

    public static LogTopic getLogTopic(String topic) {
        for (LogTopic logTopic : LogTopic.values()) {
            if (logTopic.getName().equalsIgnoreCase(topic)) {
                return logTopic;
            }
        }
        throw new IllegalArgumentException(String.format("topic %s not define", topic));
    }
}
