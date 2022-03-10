package org.mmo.engine.thread.Scene;

import io.netty.util.concurrent.ScheduledFuture;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 需要使用一个线程队列执行任务的场景
 * 
 * @see Channel
 * @author JiangZhiYong
 */
public interface Scene {

	/**
	 * 返回全局唯一id
	 * 
	 * @return
	 */
	default long id() {
		return 0;
	}

	/**
	 * Return the {@link SceneLoop} this {@link Scene} was registered to.
	 */
	SceneLoop eventLoop();

	/**
	 * Returns {@code true} if the {@link Scene} is registered with an
	 * {@link SceneLoop}.
	 */
	boolean isRegistered();

	/**
	 * 注册{@link SceneLoop}
	 * 
	 * @param sceneLoop
	 */
	void register(SceneLoop sceneLoop);

	/**
	 * 是否在当前{@link Scene} 所在线程执行
	 * 
	 * @return
	 */
	default boolean inEventLoop() {
		return eventLoop().inEventLoop();
	}

	/**
	 * 执行当前{@link Runnable} 任务，如果在当前执行线程和{@link Scene} 是同一线程则直接执行，否则放入消息队列中执行
	 * 
	 * @param runnable
	 */
	default void execute(Runnable runnable) {
		if (inEventLoop()) {
			runnable.run();
		} else {
			eventLoop().execute(runnable);
		}
	}

	/**
	 * 执行当前{@link Runnable} 任务，如果在当前执行线程和{@link Scene} 是同一线程则直接执行，否则放入消息队列中执行
	 * 
	 * @param runnable
	 * @param addQueue true 添加到队列处理，即使是同一线程
	 */
	default void execute(Runnable runnable, boolean addQueue) {
		if (addQueue || !inEventLoop()) {
			eventLoop().execute(runnable);
			return;
		} else {
			runnable.run();
		}
	}

	/**
	 * @note 测试性能不佳，任务过多cpu消耗高
	 * @param command
	 * @param delay
	 * @param unit
	 * @return
	 */
	default ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		return eventLoop().schedule(command, delay, unit);
	}

	default <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
		return eventLoop().schedule(callable, delay, unit);
	}

	default ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
		ScheduledFuture<?> scheduleAtFixedRate = eventLoop().scheduleAtFixedRate(command, initialDelay, period, unit);
		getFixedRateScheduledFutures().add(scheduleAtFixedRate);
		return scheduleAtFixedRate;
	}

	default ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
		return eventLoop().scheduleWithFixedDelay(command, initialDelay, delay, unit);
	}

	/**
	 * 当前{@link Scene} scheduleAtFixedRate 注册的ScheduledFuture
	 */
	Set<ScheduledFuture<?>> getFixedRateScheduledFutures();

	/**
	 * 取消scheduleAtFixedRate
	 */
	default void cancelFixedRateScheduledFutures() {
		getFixedRateScheduledFutures().forEach(future -> future.cancel(true));
	}
}
