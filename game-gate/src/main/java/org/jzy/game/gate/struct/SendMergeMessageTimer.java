package org.jzy.game.gate.struct;

import org.jzy.game.gate.service.GateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 发送合并消息定时器,每个线程一个定时器，而不是一个玩家一个
 * <p>
 * 经过测试分析100ms延迟不影响体验
 * </p>
 * <p>
 * <br/>
 *
 * </p>
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
public class SendMergeMessageTimer implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendMergeMessageTimer.class);
    // 该线程拥有的用户
    private List<User> users = new ArrayList<>();

    @Override
    public void run() {
        try {
            Iterator<User> iterator = users.iterator();
            while (iterator.hasNext()) {
                User user = iterator.next();
                if (!user.getClientChannel().isActive()) {
                    LOGGER.info("{} - {} connect already break,remove user", user.getPlayerId(), user.getIp());
                    iterator.remove();
                    continue;
                }
                if (user.getPackMessages().size() > 0) {
                    user.sendToUser();
                }
            }
        } catch (Exception e) {
            GateManager.getInstance().getExecutorService().showException(e, LOGGER);
        }

    }

    /**
     * 添加用户
     *
     * @param user
     */
    public void addUser(User user) {
        this.users.add(user);
    }
}
