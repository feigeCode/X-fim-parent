package com.feige.fim.api;


public interface ServerStatusListener {
    int START_SUCCESS = 1;
    int START_FAILURE = 2;
    int STOP_SUCCESS = 3;
    int STOP_FAILURE = 4;
    
    <T extends Client> void handle(Event<T> event);
}
