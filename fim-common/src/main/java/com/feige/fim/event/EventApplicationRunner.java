package com.feige.fim.event;

import com.feige.framework.context.api.ApplicationContext;
import com.feige.framework.context.api.ApplicationRunner;
import com.feige.utils.event.EventDispatcher;
import com.feige.utils.event.EventListener;
import com.feige.utils.spi.annotation.SPI;

import java.util.List;

@SPI(interfaces = ApplicationRunner.class)
public class EventApplicationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationContext applicationContext, String... args) throws Exception {
        List<EventListener> eventListeners = applicationContext.getByType(EventListener.class);
        for (EventListener eventListener : eventListeners) {
            EventDispatcher.register(eventListener);
        }
    }
}
