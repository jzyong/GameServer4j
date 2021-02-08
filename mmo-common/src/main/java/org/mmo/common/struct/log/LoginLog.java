package org.mmo.common.struct.log;

import org.mmo.common.constant.LogTopic;
import org.mmo.engine.util.TimeUtil;

/**
 * @author jzy
 * @mail 359135103@qq.com
 */
public class LoginLog implements ILog {

    private long id;

    private long playerId;

    private long loginTime;

    public LoginLog() {
    }

    public LoginLog(long id, long playerId) {
        this.id = id;
        this.playerId = playerId;
        this.loginTime = TimeUtil.currentTimeMillis();
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String topic() {
        return LogTopic.Login.getName();
    }
}
