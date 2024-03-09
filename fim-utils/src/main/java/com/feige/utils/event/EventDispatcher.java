package com.feige.utils.event;

import com.feige.utils.javassist.AnnotationUtils;
import com.feige.utils.logger.Loggers;
import com.feige.utils.thread.NameThreadFactory;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class EventDispatcher {
   
    private static EventBus eventBus;

    static {
        create(new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors() * 2,
                0L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10000),
                new NameThreadFactory("event-"),
                new ThreadPoolExecutor.CallerRunsPolicy()
        ));
    }

    public static void create(){
        eventBus = new EventBus();
    }

    public static void create(Executor executor) {
        AsyncEventBus asyncEventBus = new AsyncEventBus(executor, (exception, context)
                -> Loggers.CONSOLE.error("event bus subscriber ex", exception));

        eventBus = new SmartEventBus(asyncEventBus);
    }

    public static void fire(Object event) {
        eventBus.post(event);
    }

    public static void register(Object bean) {
        eventBus.register(bean);
    }

    public static void unregister(Object bean) {
        eventBus.unregister(bean);
    }


    static class SmartEventBus extends EventBus {

        private final AsyncEventBus asyncEventBus;

        public SmartEventBus(AsyncEventBus asyncEventBus) {
            this.asyncEventBus = asyncEventBus;
        }

        @Override
        public void register(Object object) {
            Async async = AnnotationUtils.findAnnotation(object.getClass(), Async.class);
            if (async != null){
                this.asyncEventBus.register(object);
                return;
            }
            super.register(object);
        }

        @Override
        public void unregister(Object object) {
            Async async = AnnotationUtils.findAnnotation(object.getClass(), Async.class);
            if (async != null){
                this.asyncEventBus.unregister(object);
                return;
            }
            super.unregister(object);
        }

        @Override
        public void post(Object event) {
            asyncEventBus.post(event);
            super.post(event);
        }
    }

}
