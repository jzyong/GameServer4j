package org.mmo.engine.thread.concurrent;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 插入相同元素会替换原有元素
 *
 * @param <E>
 */
public class ConcurrentCoverSetBlockingQueue<E> extends ConcurrentCoverSetQueue<E> implements BlockingQueue<E> {

    /**
     * 插入相同元素会替换原有元素
     *
     * @param capacity
     */
    public ConcurrentCoverSetBlockingQueue(final int capacity) {
        super(capacity);
    }

    public ConcurrentCoverSetBlockingQueue() {
        this(Integer.MAX_VALUE);
    }

    @Override
    public int remainingCapacity() {
        return (capacity - count.get());
    }

    @Override
    public E poll(final long timeout, final TimeUnit unit) throws InterruptedException {
        E x;
        int c;
        long nanos = unit.toNanos(timeout);
        final AtomicInteger count = this.count;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        try {
            while (count.get() == 0) {
                if (nanos <= 0) {
                    return null;
                }
                nanos = notEmpty.awaitNanos(nanos);
            }
            x = set.iterator().next();
            set.remove(x);
            c = count.getAndDecrement();
            if (c > 1) {
                notEmpty.signal();
            }
        } finally {
            takeLock.unlock();
        }
        if (c == capacity) {
            signalNotFull();
        }
        return x;
    }

    @Override
    public void put(final E e) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }

        int c;
        final ReentrantLock putLock = this.putLock;
        final AtomicInteger count = this.count;
        putLock.lockInterruptibly();
        try {
            while (count.get() == capacity) {
                notFull.await();
            }
            if (set.contains(e)) {
                set.remove(e);
                set.add(e);
                c = count.get();
            } else {
                set.add(e);
            }
            c = count.getAndIncrement();
            if (c + 1 < capacity) {
                notFull.signal();
            }
        } finally {
            putLock.unlock();
        }
        if (c == 0) {
            signalNotEmpty();
        }
    }

    @Override
    public boolean offer(final E e, final long timeout, final TimeUnit unit) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        long nanos = unit.toNanos(timeout);
        int c;
        final ReentrantLock putLock = this.putLock;
        final AtomicInteger count = this.count;
        putLock.lockInterruptibly();
        try {
            while (count.get() == capacity) {
                if (nanos <= 0) {
                    return false;
                }
                nanos = notFull.awaitNanos(nanos);
            }
            if (set.contains(e)) {
                set.remove(e);
                set.add(e);
                c = count.get();
            } else {
                set.add(e);
                c = count.getAndIncrement();
            }
            if (c + 1 < capacity) {
                notFull.signal();
            }
        } finally {
            putLock.unlock();
        }
        if (c == 0) {
            signalNotEmpty();
        }
        return true;
    }

    @Override
    public E take() throws InterruptedException {
        E x;
        int c;
        final AtomicInteger count = this.count;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        try {
            while (count.get() == 0) {
                notEmpty.await();
            }
            x = set.iterator().next();
            set.remove(x);
            c = count.getAndDecrement();
            if (c > 1) {
                notEmpty.signal();
            }
        } finally {
            takeLock.unlock();
        }
        if (c == capacity) {
            signalNotFull();
        }
        return x;
    }

    @Override
    public int drainTo(final Collection<? super E> c) {
        return drainTo(c, Integer.MAX_VALUE);
    }

    @Override
    public int drainTo(final Collection<? super E> c, final int maxElements) {
        if (c == null) {
            throw new NullPointerException();
        }
        if (c == this) {
            throw new IllegalArgumentException();
        }
        if (maxElements <= 0) {
            return 0;
        }
        boolean signalNotFull = false;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            int n = Math.min(maxElements, count.get());
            int i = 0;
            try {
                while (i < n) {
                    E e = set.iterator().next();
                    set.remove(e);
                    c.add(e);
                    ++i;
                }
                return n;
            } finally {
                if (i > 0) {
                    signalNotFull = (count.getAndAdd(-i) == capacity);
                }
            }
        } finally {
            takeLock.unlock();
            if (signalNotFull) {
                signalNotFull();
            }
        }
    }
}
