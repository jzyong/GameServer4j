package org.mmo.engine.thread.Scene;

import io.netty.util.concurrent.ScheduledFuture;

import java.util.HashSet;
import java.util.Set;

/**
 * 抽象{@link Scene} 封装
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public abstract class AbstractScene implements Scene {
	
	private SceneLoop sceneLoop;
	
	private Set<ScheduledFuture<?>> fixedRateScheduledFutures=new HashSet<ScheduledFuture<?>>();


	@Override
	public SceneLoop eventLoop() {
		return sceneLoop;
	}

	@Override
	public boolean isRegistered() {
		return sceneLoop!=null;
	}

	@Override
	public void register(SceneLoop sceneLoop) {
		this.sceneLoop=sceneLoop;

	}

	@Override
	public Set<ScheduledFuture<?>> getFixedRateScheduledFutures() {
		return this.fixedRateScheduledFutures;
	}
	

}
