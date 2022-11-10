package org.mmo.engine.thread.Scene;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * 执行场景任务线程组{@link Scene}
 * 
 * @see NioEventLoopGroup
 * @author JiangZhiYong
 * @date 2019年5月15日 下午7:39:43
 * @mail 359135103@qq.com
 */
public class SceneTaskLoopGroup extends MultithreadSceneLoopGroup {

	/**
	 * Create a new instance using the default number of threads, the default
	 * {@link ThreadFactory}
	 */
	public SceneTaskLoopGroup() {
		this(0);
	}

	/**
	 * Create a new instance using the specified number of threads,
	 * {@link ThreadFactory}
	 */
	public SceneTaskLoopGroup(int nThreads) {
		this(nThreads, (Executor) null);
	}

	public SceneTaskLoopGroup(int nThreads, Executor executor) {
		super(nThreads, executor);
	}

	public SceneTaskLoopGroup(int nThreads, ThreadFactory threadFactory) {
		super(nThreads, threadFactory);
	}


	@Override
	protected SceneLoop newChild(Executor executor, Object... args) throws Exception {
		return new SceneTaskLoop(this, executor);
	}

}
