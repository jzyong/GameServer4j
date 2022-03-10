package org.mmo.engine.thread.concurrent;

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 相同元素不能插入
 * @param <E>
 */
public abstract class ConcurrentSetQueue<E> extends ConcurrentSetCollection<E> implements Queue<E> {

    ConcurrentSetQueue(final int capacity) {
        super(capacity);
    }

    @Override
    public boolean offer(final E e) {
        return super.offer(e);
    }

    @Override
    public E poll() {
        final AtomicInteger count = this.count;
        if (count.get() == 0) {
            return null;
        }
        E x = null;
        int c = -1;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            if (count.get() > 0) {
                x = set.iterator().next();
                set.spliterator();
                set.remove(x);
                c = count.getAndDecrement();
                if (c > 1) {
                    notEmpty.signal();
                }
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
    public E element() {
        E x = peek();
        if (x != null) {
            return x;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public E peek() {
        if (count.get() == 0) {
            return null;
        }
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            return set.iterator().next();
        } finally {
            takeLock.unlock();
        }
    }

    @Override
    public E remove() {
        E x = poll();
        if (x != null) {
            return x;
        } else {
            throw new NoSuchElementException();
        }
    }
}
