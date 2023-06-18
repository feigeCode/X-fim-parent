package com.feige.fim.listener;

import com.feige.fim.api.Client;
import com.feige.fim.api.Event;
import com.feige.fim.api.ServerStatusListener;

public class DefaultServerStatusListener implements ServerStatusListener {
    
    private static DefaultServerStatusListener serverStatusListener;
    
    public static DefaultServerStatusListener getInstance(){
        if (serverStatusListener == null){
            synchronized (DefaultServerStatusListener.class){
                if (serverStatusListener == null) {
                    serverStatusListener = new DefaultServerStatusListener();
                }
            }
        }
        return serverStatusListener;
    }
    
    private DefaultServerStatusListener(){
        
    }
    
    @Override
    public <T extends Client> void handle(Event<T> event) {
        System.out.println(event);
    }
}
