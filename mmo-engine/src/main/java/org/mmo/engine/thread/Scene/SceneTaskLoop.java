package org.mmo.engine.thread.Scene;

import io.netty.util.concurrent.AbstractEventExecutor;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * 执行场景对象中的任务，在一个线程中顺序执行
 * 
 * @see NioEventLoop
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public final class SceneTaskLoop extends SingleThreadTaskLoop {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractEventExecutor.class);
	private static final Logger LOGGER = LoggerFactory.getLogger(SceneTaskLoop.class);

	public SceneTaskLoop() {
		this((SceneLoopGroup) null);
	}

	public SceneTaskLoop(ThreadFactory threadFactory) {
		this(null, threadFactory);
	}

	public SceneTaskLoop(Executor executor) {
		this(null, executor);
	}

	public SceneTaskLoop(SceneLoopGroup parent) {
		this(parent, new DefaultThreadFactory(SceneTaskLoop.class));
	}

	public SceneTaskLoop(SceneLoopGroup parent, ThreadFactory threadFactory) {
		super(parent, threadFactory, true);
	}

	public SceneTaskLoop(SceneLoopGroup parent, Executor executor) {
		super(parent, executor, true);
	}

	@Override
	protected void run() {
		for (;;) {
			Runnable task = takeTask();
			if (task != null) {
				long startTime = System.currentTimeMillis();
				safeExecute(task);
				if (System.currentTimeMillis() - startTime > 50) {
					LOGGER.warn("任务[{}-{}]执行：{}ms 剩余任务：{}", task.getClass().getSimpleName(), task.toString(),
							(System.currentTimeMillis() - startTime), this.pendingTasks());
				}
				updateLastExecutionTime();
			}

			if (confirmShutdown()) {
				break;
			}
		}
	}

	@Override
	protected void cleanup() {
		super.cleanup();
		LOGGER.error("线程[{}] 关闭", this.threadProperties().name());

	}

	protected static void safeExecute(Runnable task) {
		try {
			task.run();
		} catch (Throwable t) {
			StringBuffer sb = new StringBuffer();
			sb.append(t.getClass().getSimpleName()).append(":");
			sb.append(t.getMessage()).append("\n");
			for (StackTraceElement stackTraceElement : t.getStackTrace()) {
				sb.append(stackTraceElement.toString()).append("\n");
			}
			LOGGER.error("任务[{}] Exception：{}", task.getClass().getSimpleName(), sb.toString());
			logger.error("A task raised an exception. Task: {}", task, t);
		}
	}
}
