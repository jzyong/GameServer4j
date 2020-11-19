package org.mmo.engine.thread;

import io.netty.util.concurrent.SingleThreadEventExecutor;
import org.mmo.engine.thread.Scene.Scene;
import org.mmo.engine.thread.Scene.SceneLoop;
import org.mmo.engine.thread.Scene.SceneLoopGroup;
import org.mmo.engine.thread.Scene.SceneTaskLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程池服务
 * <br>
 *     默认注册了io
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public abstract class AbstractExecutorService implements IExecutorService{
    private static final Logger LOGGER= LoggerFactory.getLogger(AbstractExecutorService.class);

    private final Map<String, Executor> executors=new ConcurrentHashMap<>();

    @Autowired
    private ExecutorProperties executorProperties;

    private SceneLoopGroup soleTaskLoopGroup;

    @PostConstruct
    public void init(){
        //注册无序默认IO线程池
        MyThreadFactory factory = new MyThreadFactory(new ThreadGroup("game"),executorProperties.getName());
        Executor defaultExecutor;
        if (executorProperties.getCorePoolSize() == executorProperties.getMaximumPoolSize() && executorProperties.getCorePoolSize() == 1) {
            defaultExecutor = Executors.newSingleThreadExecutor(factory);
        } else {
            defaultExecutor = new ThreadPoolExecutor(executorProperties.getCorePoolSize(), executorProperties.getMaximumPoolSize(), executorProperties.getKeepAliveTime(),
                    TimeUnit.MINUTES, new LinkedBlockingDeque<>(executorProperties.getMaxCommandSize()),
                    factory,
                    new ThreadPoolExecutor.CallerRunsPolicy()
            );
        }
        register("io",defaultExecutor);

        //顺序执行的线程组
        factory = new MyThreadFactory(new ThreadGroup("game"),"sole");
        soleTaskLoopGroup=new SceneTaskLoopGroup(0,factory);
    }

    @PreDestroy
    public void destroy(){
        soleTaskLoopGroup.shutdownGracefully();
    }

    @Override
    public void register(String name, Executor executor) {
        executors.put(name,executor);
    }


    @Override
    public Executor getExecutor(String threadName) {
        return executors.get(threadName);
    }

    @Override
    public void execute(String threadName, Runnable runnable) {
        Executor executor = getExecutor(threadName);
        if(executor==null){
            LOGGER.warn("线程 {} 未注册",threadName);
            return;
        }
        executor.execute(runnable);
    }

    /**
     * 注册 scene 线程
     *
     * @param threadName
     * @param scene
     */
    public void registerScene(String threadName, Scene scene) {
        SceneLoop sceneLoop = soleTaskLoopGroup.next();
        LOGGER.info("{} {} 注册到{} 线程 ", threadName, scene.getClass().getSimpleName(),
                ((SingleThreadEventExecutor) sceneLoop).threadProperties().name());
        scene.register(sceneLoop);
        executors.put(threadName, scene.eventLoop());
    }


}
