package org.mmo.engine.thread.Scene;

import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorChooserFactory;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 *   Abstract base class for {@link SceneLoopGroup} implementations that handles their tasks with multiple threads at
 * the same time.
 * @see MultithreadEventLoopGroup
 * @author JiangZhiYong
 * @date 2019年5月15日 下午6:13:56
 * @mail 359135103@qq.com
 */
public abstract class MultithreadSceneLoopGroup extends MultithreadEventExecutorGroup implements SceneLoopGroup {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(MultithreadSceneLoopGroup.class);

    /**默认线程数*/
    private static final int DEFAULT_SCENE_LOOP_THREADS;

    static {
        DEFAULT_SCENE_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt(
                "com.game.sceneLoopThreads", NettyRuntime.availableProcessors() * 2));

        if (logger.isDebugEnabled()) {
            logger.debug("-Dio.netty.eventLoopThreads: {}", DEFAULT_SCENE_LOOP_THREADS);
        }
    }

    /**
     * @see MultithreadEventExecutorGroup#MultithreadEventExecutorGroup(int, Executor, Object...)
     */
    protected MultithreadSceneLoopGroup(int nThreads, Executor executor, Object... args) {
        super(nThreads == 0 ? DEFAULT_SCENE_LOOP_THREADS : nThreads, executor, args);
    }

    /**
     * @see MultithreadEventExecutorGroup#MultithreadEventExecutorGroup(int, ThreadFactory, Object...)
     */
    protected MultithreadSceneLoopGroup(int nThreads, ThreadFactory threadFactory, Object... args) {
        super(nThreads == 0 ? DEFAULT_SCENE_LOOP_THREADS : nThreads, threadFactory, args);
    }

    /**
     * @see MultithreadEventExecutorGroup#MultithreadEventExecutorGroup(int, Executor,
     * EventExecutorChooserFactory, Object...)
     */
    protected MultithreadSceneLoopGroup(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory,
                                     Object... args) {
        super(nThreads == 0 ? DEFAULT_SCENE_LOOP_THREADS : nThreads, executor, chooserFactory, args);
    }

    @Override
    protected ThreadFactory newDefaultThreadFactory() {
        return new DefaultThreadFactory(getClass(), Thread.MAX_PRIORITY);
    }

    @Override
    public SceneLoop next() {
        return (SceneLoop) super.next();
    }

    @Override
    protected abstract SceneLoop newChild(Executor executor, Object... args) throws Exception;

    @Override
    public SceneFuture register(Scene scene) {
    	//TODO 自定义实现选择 线程 ，游戏野外地图消息量比较大，独占线程 ？？？
        return next().register(scene);
    }

    @Override
    public SceneFuture register(ScenePromise promise) {
        return next().register(promise);
    }

}
