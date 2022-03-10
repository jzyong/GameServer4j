package org.mmo.engine.thread;

import org.mmo.engine.thread.Scene.Scene;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

/**
 * 获取调度线程
 */
@Service
public interface IExecutorService {


    /**
     * 获取执行线程
     * @param threadName
     * @return
     */
     Executor getExecutor(String threadName);

    /**
     * 注册执行线程
     * @param name
     * @param executor
     */
     void register(String name, Executor executor);

    /**
     * 执行任务
     * @param threadName
     * @param runnable
     */
     void execute(String threadName, Runnable runnable);

    /**
     * 注册线程使用场景
     * @param threadName
     * @param scene
     */
    public void registerScene(String threadName, Scene scene);
}
