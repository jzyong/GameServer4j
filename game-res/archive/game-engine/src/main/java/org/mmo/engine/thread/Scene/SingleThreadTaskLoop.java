package org.mmo.engine.thread.Scene;

import io.netty.util.concurrent.RejectedExecutionHandler;
import io.netty.util.concurrent.RejectedExecutionHandlers;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.UnstableApi;

import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * Abstract base class for {@link SceneLoop}s that execute all its submitted
 * tasks in a single thread.
 * 
 * @author JiangZhiYong
 * @date 2019年5月15日 下午8:13:56
 * @mail 359135103@qq.com
 */
public abstract class SingleThreadTaskLoop extends SingleThreadEventExecutor implements SceneLoop {

	protected static final int DEFAULT_MAX_PENDING_TASKS = Math.max(16,
			SystemPropertyUtil.getInt("com.game.sceneTaskLoop.maxPendingTasks", Integer.MAX_VALUE));

	private final Queue<Runnable> tailTasks;

	protected SingleThreadTaskLoop(SceneLoopGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp) {
		this(parent, threadFactory, addTaskWakesUp, DEFAULT_MAX_PENDING_TASKS, RejectedExecutionHandlers.reject());
	}

	protected SingleThreadTaskLoop(SceneLoopGroup parent, Executor executor, boolean addTaskWakesUp) {
		this(parent, executor, addTaskWakesUp, DEFAULT_MAX_PENDING_TASKS, RejectedExecutionHandlers.reject());
	}

	protected SingleThreadTaskLoop(SceneLoopGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp,
			int maxPendingTasks, RejectedExecutionHandler rejectedExecutionHandler) {
		super(parent, threadFactory, addTaskWakesUp, maxPendingTasks, rejectedExecutionHandler);
		tailTasks = newTaskQueue(maxPendingTasks);
	}

	protected SingleThreadTaskLoop(SceneLoopGroup parent, Executor executor, boolean addTaskWakesUp,
			int maxPendingTasks, RejectedExecutionHandler rejectedExecutionHandler) {
		super(parent, executor, addTaskWakesUp, maxPendingTasks, rejectedExecutionHandler);
		tailTasks = newTaskQueue(maxPendingTasks);
	}

	@Override
	public SceneLoopGroup parent() {
		return (SceneLoopGroup) super.parent();
	}

	@Override
	public SceneLoop next() {
		return (SceneLoop) super.next();
	}

	@Override
	public SceneFuture register(Scene scene) {
		return register(new DefaultScenePromise(scene, this));
	}

	@Override
	public SceneFuture register(final ScenePromise promise) {
		ObjectUtil.checkNotNull(promise, "promise");
		promise.scene().register(this);
		return promise;
	}

	/**
	 * Adds a task to be run once at the end of next (or current) {@code eventloop}
	 * iteration.
	 *
	 * @param task to be added.
	 */
	@UnstableApi
	public final void executeAfterEventLoopIteration(Runnable task) {
		ObjectUtil.checkNotNull(task, "task");
		if (isShutdown()) {
			reject();
		}

		if (!tailTasks.offer(task)) {
			reject(task);
		}

		if (wakesUpForTask(task)) {
			wakeup(inEventLoop());
		}
	}

	/**
	 * Removes a task that was added previously via
	 * {@link #executeAfterEventLoopIteration(Runnable)}.
	 *
	 * @param task to be removed.
	 *
	 * @return {@code true} if the task was removed as a result of this call.
	 */
	@UnstableApi
	final boolean removeAfterEventLoopIterationTask(Runnable task) {
		return tailTasks.remove(ObjectUtil.checkNotNull(task, "task"));
	}

	@Override
	protected boolean wakesUpForTask(Runnable task) {
		return !(task instanceof NonWakeupRunnable);
	}

	@Override
	protected void afterRunningAllTasks() {
		runAllTasksFrom(tailTasks);
	}

	@Override
	protected boolean hasTasks() {
		return super.hasTasks() || !tailTasks.isEmpty();
	}

	@Override
	public int pendingTasks() {
		return super.pendingTasks() + tailTasks.size();
	}

	/**
	 * Marker interface for {@link Runnable} that will not trigger an
	 * {@link #wakeup(boolean)} in all cases.
	 */
	interface NonWakeupRunnable extends Runnable {
	}
}
