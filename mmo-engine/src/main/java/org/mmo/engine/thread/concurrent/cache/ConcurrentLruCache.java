package org.mmo.engine.thread.concurrent.cache;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.EvictionListener;
import com.googlecode.concurrentlinkedhashmap.Weighers;

import java.util.Set;

public class ConcurrentLruCache<K, V> {
	private ConcurrentLinkedHashMap<K, V> cache;
    private static final int              DEFAULT_CAPACITY         =  389;
    public static final int               DEFAULT_CONCURENCY_LEVEL = 64;
 
    public ConcurrentLruCache(){
        this(DEFAULT_CAPACITY);
    }
 
    public ConcurrentLruCache(int capacity){
        this(capacity,null);
    }
 
    public ConcurrentLruCache(int capacity, EvictionListener<K, V> listener){
        if (capacity <= 0) {
            capacity = DEFAULT_CAPACITY;
        }
        ConcurrentLinkedHashMap.Builder<K, V> builder = new ConcurrentLinkedHashMap.Builder<K, V>();
        builder.maximumWeightedCapacity(capacity);
        builder.weigher(Weighers.singleton());
        builder.concurrencyLevel(DEFAULT_CONCURENCY_LEVEL);
        if(listener != null) builder.listener(listener);
        cache = builder.build();
    }
 
    public long capacity() {
        return cache.capacity();
    }
 
    public boolean isEmpty() {
        return cache.isEmpty();
    }
 
    public int size() {
        return cache.size();
    }
 
    public void clear() {
        cache.clear();
    }
 
    public V get(Object key) {
        V v = cache.get(key);
        return v;
    }
 
    public void put(K key, V value) {
        cache.put(key, value);
    }
 
    public boolean putIfAbsent(K key, V value) {
        return cache.putIfAbsent(key, value) == null;
    }
 
    public boolean replace(K key, V old, V value) {
        return cache.replace(key, old, value);
    }
 
    public void remove(K key) {
        cache.remove(key);
    }
 
    public Set<K> keySet() {
        return cache.keySet();
    }
    public Set<K> hotKeySet(int n) {
        return cache.descendingKeySetWithLimit(n);
    }
 
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }
}
