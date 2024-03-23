package com.feige.utils.event;

import com.feige.utils.javassist.AnnotationUtils;
import com.feige.utils.logger.Loggers;
import com.feige.utils.thread.ExecutorFactory;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executor;

public class EventDispatcher {
   
    private static EventBus eventBus;

    static {
        create(ExecutorFactory.createCachedThreadPool("event",
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors() * 2,
                60L,
                10000
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
