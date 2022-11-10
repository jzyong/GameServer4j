package org.mmo.engine.thread.Scene;

import io.netty.util.concurrent.OrderedEventExecutor;

/**
 * 处理所有{@link Scene}任务，注册到当前执行器，一个{@link SceneLoop }可以注册多个{@link Scene}
 * @see EventLoop
 * @author JiangZhiYong
 * @date 2019年5月15日 下午4:07:15
 * @mail 359135103@qq.com
 */
public interface SceneLoop extends SceneLoopGroup, OrderedEventExecutor {

	@Override
	SceneLoopGroup parent();
}
