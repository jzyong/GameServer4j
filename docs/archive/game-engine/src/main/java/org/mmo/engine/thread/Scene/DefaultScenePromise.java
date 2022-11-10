package org.mmo.engine.thread.Scene;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import static io.netty.util.internal.ObjectUtil.checkNotNull;

/**
 * The default {@link ScenePromise} implementation.  
 */
public class DefaultScenePromise extends DefaultPromise<Void> implements ScenePromise {

    private final Scene scene;
    private long checkpoint;

    /**
     * Creates a new instance.
     *
     * @param scene
     *        the {@link Channel} associated with this future
     */
    public DefaultScenePromise(Scene scene) {
        this.scene = checkNotNull(scene, "scene");
    }

    /**
     * Creates a new instance.
     *
     * @param channel
     *        the {@link Channel} associated with this future
     */
    public DefaultScenePromise(Scene scene, EventExecutor executor) {
        super(executor);
        this.scene = checkNotNull(scene, "scene");
    }

    @Override
    protected EventExecutor executor() {
        EventExecutor e = super.executor();
        if (e == null) {
            return scene().eventLoop();
        } else {
            return e;
        }
    }

    @Override
    public Scene scene () {
        return scene;
    }

    @Override
    public ScenePromise setSuccess() {
        return setSuccess(null);
    }

    @Override
    public ScenePromise setSuccess(Void result) {
        super.setSuccess(result);
        return this;
    }

    @Override
    public boolean trySuccess() {
        return trySuccess(null);
    }

    @Override
    public ScenePromise setFailure(Throwable cause) {
        super.setFailure(cause);
        return this;
    }

    @Override
    public ScenePromise addListener(GenericFutureListener<? extends Future<? super Void>> listener) {
        super.addListener(listener);
        return this;
    }

    @Override
    public ScenePromise addListeners(GenericFutureListener<? extends Future<? super Void>>... listeners) {
        super.addListeners(listeners);
        return this;
    }

    @Override
    public ScenePromise removeListener(GenericFutureListener<? extends Future<? super Void>> listener) {
        super.removeListener(listener);
        return this;
    }

    @Override
    public ScenePromise removeListeners(GenericFutureListener<? extends Future<? super Void>>... listeners) {
        super.removeListeners(listeners);
        return this;
    }

    @Override
    public ScenePromise sync() throws InterruptedException {
        super.sync();
        return this;
    }

    @Override
    public ScenePromise syncUninterruptibly() {
        super.syncUninterruptibly();
        return this;
    }

    @Override
    public ScenePromise await() throws InterruptedException {
        super.await();
        return this;
    }

    @Override
    public ScenePromise awaitUninterruptibly() {
        super.awaitUninterruptibly();
        return this;
    }



    @Override
    protected void checkDeadLock() {
        if (scene().isRegistered()) {
            super.checkDeadLock();
        }
    }

    @Override
    public ScenePromise unvoid() {
        return this;
    }

    @Override
    public boolean isVoid() {
        return false;
    }
}