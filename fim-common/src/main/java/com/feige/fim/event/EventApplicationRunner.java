package com.feige.fim.event;

import com.feige.framework.context.api.ApplicationContext;
import com.feige.framework.context.api.ApplicationRunner;
import com.feige.utils.event.EventDispatcher;
import com.feige.utils.event.EventListener;
import com.feige.utils.spi.annotation.SPI;
import lombok.Setter;

import java.util.List;

@SPI(interfaces = ApplicationRunner.class)
@Setter
public class EventApplicationRunner implements ApplicationRunner {

    private List<EventListener> eventListeners;

    @Override
    public void run(ApplicationContext applicationContext, String... args) throws Exception {
        for (EventListener eventListener : eventListeners) {
            EventDispatcher.register(eventListener);
        }
    }
}
