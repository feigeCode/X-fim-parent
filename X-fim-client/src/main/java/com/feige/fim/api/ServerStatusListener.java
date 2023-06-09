package com.feige.fim.api;


public interface ServerStatusListener {
    
    void handle(Event<Client> event);
}
