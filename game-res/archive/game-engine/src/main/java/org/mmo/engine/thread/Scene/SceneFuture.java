package org.mmo.engine.thread.Scene;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.TimeUnit;


/**
 * The result of an asynchronous {@link Scene} I/O operation.
 * <p>
 * All I/O operations are asynchronous.  It means any I/O calls will
 * return immediately with no guarantee that the requested I/O operation has
 * been completed at the end of the call.  Instead, you will be returned with
 * a {@link SceneFuture} instance which gives you the information about the
 * result or status of the I/O operation.
 * <p>
 * A {@link SceneFuture} is either <em>uncompleted</em> or <em>completed</em>.
 * When an I/O operation begins, a new future object is created.  The new future
 * is uncompleted initially - it is neither succeeded, failed, nor cancelled
 * because the I/O operation is not finished yet.  If the I/O operation is
 * finished either successfully, with failure, or by cancellation, the future is
 * marked as completed with more specific information, such as the cause of the
 * failure.  Please note that even failure and cancellation belong to the
 * completed state.
 * <pre>
 *                                      +---------------------------+
 *                                      | Completed successfully    |
 *                                      +---------------------------+
 *                                 +---->      isDone() = true      |
 * +--------------------------+    |    |   isSuccess() = true      |
 * |        Uncompleted       |    |    +===========================+
 * +--------------------------+    |    | Completed with failure    |
 * |      isDone() = false    |    |    +---------------------------+
 * |   isSuccess() = false    |----+---->      isDone() = true      |
 * | isCancelled() = false    |    |    |       cause() = non-null  |
 * |       cause() = null     |    |    +===========================+
 * +--------------------------+    |    | Completed by cancellation |
 *                                 |    +---------------------------+
 *                                 +---->      isDone() = true      |
 *                                      | isCancelled() = true      |
 *                                      +---------------------------+
 * </pre>
 *
 * Various methods are provided to let you check if the I/O operation has been
 * completed, wait for the completion, and retrieve the result of the I/O
 * operation. It also allows you to add {@link SceneFutureListener}s so you
 * can get notified when the I/O operation is completed.
 *
 * <h3>Prefer {@link #addListener(GenericFutureListener)} to {@link #await()}</h3>
 *
 * It is recommended to prefer {@link #addListener(GenericFutureListener)} to
 * {@link #await()} wherever possible to get notified when an I/O operation is
 * done and to do any follow-up tasks.
 * <p>
 * {@link #addListener(GenericFutureListener)} is non-blocking.  It simply adds
 * the specified {@link SceneFutureListener} to the {@link SceneFuture}, and
 * I/O thread will notify the listeners when the I/O operation associated with
 * the future is done.  {@link SceneFutureListener} yields the best
 * performance and resource utilization because it does not block at all, but
 * it could be tricky to implement a sequential logic if you are not used to
 * event-driven programming.
 * <p>
 * By contrast, {@link #await()} is a blocking operation.  Once called, the
 * caller thread blocks until the operation is done.  It is easier to implement
 * a sequential logic with {@link #await()}, but the caller thread blocks
 * unnecessarily until the I/O operation is done and there's relatively
 * expensive cost of inter-thread notification.  Moreover, there's a chance of
 * dead lock in a particular circumstance, which is described below.
 *
 * <h3>Do not call {@link #await()} inside {@link ChannelHandler}</h3>
 * <p>
 * The event handler methods in {@link ChannelHandler} are usually called by
 * an I/O thread.  If {@link #await()} is called by an event handler
 * method, which is called by the I/O thread, the I/O operation it is waiting
 * for might never complete because {@link #await()} can block the I/O
 * operation it is waiting for, which is a dead lock.

 * 
 * @see ChannelFuture
 */
public interface SceneFuture extends  Future<Void>{

	   /**
     * Returns a Scene where the I/O operation associated with this
     * future takes place.
     */
    Scene scene();

    @Override
    SceneFuture addListener(GenericFutureListener<? extends Future<? super Void>> listener);

    @Override
    SceneFuture addListeners(GenericFutureListener<? extends Future<? super Void>>... listeners);

    @Override
    SceneFuture removeListener(GenericFutureListener<? extends Future<? super Void>> listener);

    @Override
    SceneFuture removeListeners(GenericFutureListener<? extends Future<? super Void>>... listeners);

    @Override
    SceneFuture sync() throws InterruptedException;

    @Override
    SceneFuture syncUninterruptibly();

    @Override
    SceneFuture await() throws InterruptedException;

    @Override
    SceneFuture awaitUninterruptibly();

    /**
     * Returns {@code true} if this {@link SceneFuture} is a void future and so not allow to call any of the
     * following methods:
     * <ul>
     *     <li>{@link #addListener(GenericFutureListener)}</li>
     *     <li>{@link #addListeners(GenericFutureListener[])}</li>
     *     <li>{@link #await()}</li>
     *     <li>{@link #await(long, TimeUnit)} ()}</li>
     *     <li>{@link #await(long)} ()}</li>
     *     <li>{@link #awaitUninterruptibly()}</li>
     *     <li>{@link #sync()}</li>
     *     <li>{@link #syncUninterruptibly()}</li>
     * </ul>
     */
    boolean isVoid();
}
