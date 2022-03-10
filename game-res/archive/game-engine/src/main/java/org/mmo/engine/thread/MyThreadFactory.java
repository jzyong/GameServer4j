package org.mmo.engine.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程工厂
 * @author JiangZhiYong
 * @date 2019/04/16
 */
public class MyThreadFactory implements ThreadFactory {

    protected static final Logger LOGGER = LoggerFactory.getLogger(MyThreadFactory.class);
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private static final AtomicInteger threadNumber = new AtomicInteger(1);

    private final ThreadGroup group;
    private final String namePrefix;

    public MyThreadFactory(ThreadGroup group) {
        this.group = group;
        namePrefix = setNamePreFix(group, "");
    }
    
    public MyThreadFactory(ThreadGroup group, String prefix) {
        this.group = group;
        this.namePrefix = setNamePreFix(group, prefix);
        
    }
    public String setNamePreFix(ThreadGroup group, String prefix) {
    	return group.getName()+ prefix + " pool-"+ poolNumber.getAndIncrement()+ "-thread-";
    }
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        //t.setUncaughtExceptionHandler(eh);
        return t;
    }
}
