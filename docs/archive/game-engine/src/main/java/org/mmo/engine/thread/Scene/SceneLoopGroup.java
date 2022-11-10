package org.mmo.engine.thread.Scene;

import io.netty.util.concurrent.EventExecutorGroup;

/**
 *  特殊的{@link EventExecutorGroup} ,允许注册{@link Scene}
 * <br>
 *  使用此接口调用的任务将在线程池中选择一个任务执行，不能保证顺序
 *  @see EventLoopGroup
 * @author JiangZhiYong
 * @date 2019年5月15日 下午4:04:52
 * @mail 359135103@qq.com
 */
public interface SceneLoopGroup extends EventExecutorGroup{

	@Override
	SceneLoop next();
	
    /**
     * Register a {@link Scene} with this {@link SceneLoop}. The returned {@link SceneFuture}
     * will get notified once the registration was complete.
     */
    SceneFuture register(Scene scene);

    /**
     * Register a {@link Scene} with this {@link SceneLoop} using a {@link SceneFuture}. The passed
     * {@link SceneFuture} will get notified once the registration was complete and also will get returned.
     */
    SceneFuture register(ScenePromise promise);
}
