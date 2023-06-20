package com.feige.fim.utils;

import com.feige.fim.lg.Loggers;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executor;

public class EventDispatcher {
   
    private static EventBus eventBus;

    public static void create(Executor executor) {
        eventBus = new AsyncEventBus(executor, (exception, context)
                -> Loggers.CONSOLE.error("event bus subscriber ex", exception));
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

}
