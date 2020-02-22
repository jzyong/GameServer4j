package org.mmo.log.sl4j;

import org.mmo.engine.util.JsonUtil;

/**
 * 服务器日志
 */
public class ServerLog {
    private String time;
    private String thread;
    private String logLevel;
    private String logString;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getLogString() {
        return logString;
    }

    public void setLogString(String logString) {
        this.logString = logString;
    }

    @Override
    public String toString() {
        return JsonUtil.toJSONString(this);
    }
}
