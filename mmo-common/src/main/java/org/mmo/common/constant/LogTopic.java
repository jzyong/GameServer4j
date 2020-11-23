package org.mmo.common.constant;

/**
 * 日志话题
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
}
