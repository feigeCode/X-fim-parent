package com.feige.fim.listener;

import com.feige.fim.api.Client;
import com.feige.fim.api.Event;
import com.feige.fim.api.ServerStatusListener;
import com.feige.fim.event.ClientEvent;
import com.feige.fim.push.PushManager;

public class DefaultServerStatusListener implements ServerStatusListener {
    
    @Override
    public <T extends Client> void handle(Event<T> event) {
        System.out.println(event.getType());
        if (event instanceof ClientEvent){
            T source = event.getSource();
            if (event.getType() == ServerStatusListener.START_SUCCESS){
                PushManager.getInstance().setPushService(source.getPushService());
            }
        }
    }
}
