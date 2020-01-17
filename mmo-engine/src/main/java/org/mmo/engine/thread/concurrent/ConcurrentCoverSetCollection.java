package org.mmo.engine.thread.concurrent;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 插入相同元素会替换原有元素
 *
 * @param <E>
 */
public class ConcurrentCoverSetCollection<E> extends AbstractConcurrentSet<E> implements Collection<E> {

    ConcurrentCoverSetCollection(final int capacity) {
        super(capacity);
    }

    @Override
    public int size() {
        return count.get();
    }

    @Override
    public void clear() {
        fullyLock();
        try {
            set.clear();
        } finally {
            fullyUnlock();
        }

    }

    @Override
    public boolean isEmpty() {
        return count.get() == 0;
    }

    public boolean offer(final E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        final AtomicInteger count = this.count;
        if (count.get() == capacity) {
            throw new IllegalStateException("Queue full, count:" + count.get());
        }
        int c = -1;
        fullyLock();
        try {
            if (count.get() < capacity) {
                if (!set.contains(e)) {
                    c = count.getAndIncrement();
                    set.add(e);
                } else {
                    set.remove(e);
                    set.add(e);
                    c = count.get();
                }
                if (c + 1 < capacity) {
                    notFull.signal();
                }
            }
        } finally {
            fullyUnlock();
        }
        if (c == 0) {
            signalNotEmpty();
        }
        return c >= 0;
    }

    @Override
    public boolean add(final E e) {
        return this.offer(e);
    }

    @Override
    public boolean remove(final Object o) {
        if (o == null) {
            return false;
        }
        fullyLock();
        try {
            if (set.remove(o)) {
                count.decrementAndGet();
                return true;
            }
            return false;
        } finally {
            fullyUnlock();
        }
    }

    @Override
    public boolean contains(final Object o) {
        if (o == null) {
            return false;
        }
        fullyLock();
        try {
            return set.contains(o);
        } finally {
            fullyUnlock();
        }
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        for (Object o : c) {
            if (!this.contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return new CopyOnWriteArrayList<>(set).iterator();
    }

    @Override
    public Object[] toArray() {
        fullyLock();
        try {
            return set.toArray();
        } finally {
            fullyUnlock();
        }
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        fullyLock();
        try {
            return set.toArray(a);
        } finally {
            fullyUnlock();
        }
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}
