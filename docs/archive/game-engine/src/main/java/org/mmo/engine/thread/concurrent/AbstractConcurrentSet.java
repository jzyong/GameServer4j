package org.mmo.engine.thread.concurrent;

import java.util.LinkedHashSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Abstract concurrent class holding a set and methods to handle concurrent
 * locks. The {@link java.util.Set} is a {@link LinkedHashSet}, it guaranties
 * unity, and is aware of the order of insertion inside the
 * {@link java.util.Set}, so we are able to simulate a "first in first out"
 * strategy. Inspired by {@link java.util.concurrent.LinkedBlockingQueue}
 *
 * @param <E>
 */
abstract class AbstractConcurrentSet<E> {

    /**
     * Maximum capacity of the queue
     */
    final int capacity;

    /**
     * Current number of elements
     */
    final AtomicInteger count = new AtomicInteger();

    /**
     * Lock held by take, poll, etc
     */
    final ReentrantLock takeLock = new ReentrantLock();

    /**
     * Wait queue for waiting takes
     */
    final Condition notEmpty = takeLock.newCondition();

    /**
     * Lock held by put, offer, etc
     */
    final ReentrantLock putLock = new ReentrantLock();

    /**
     * Wait queue for waiting puts
     */
    final Condition notFull = putLock.newCondition();

    /**
     * Set backing the queue
     */
    final LinkedHashSet<E> set;

    /**
     * Instantiate a concurrent set with a maximum capacity of capacity
     */
    AbstractConcurrentSet(final int capacity) {
        this.capacity = capacity;
        this.set = new LinkedHashSet<>();
    }

    /**
     * Signals a waiting take. Called only from put/offer (which do not
     * otherwise ordinarily lock takeLock.)
     */
    void signalNotEmpty() {
        takeLock.lock();
        try {
            notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
    }

    /**
     * Signals a waiting put. Called only from take/poll.
     */
    void signalNotFull() {
        putLock.lock();
        try {
            notFull.signal();
        } finally {
            putLock.unlock();
        }
    }

    /**
     * Locks to prevent both puts and takes.
     */
    void fullyLock() {
        putLock.lock();
        takeLock.lock();
    }

    /**
     * Unlocks to allow both puts and takes.
     */
    void fullyUnlock() {
        takeLock.unlock();
        putLock.unlock();
    }
}
