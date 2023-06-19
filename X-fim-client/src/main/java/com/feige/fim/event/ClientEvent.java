package com.feige.fim.event;

import com.feige.fim.api.Client;
import com.feige.fim.api.Event;

public class ClientEvent implements Event<Client> {
    private final Client client;
    private final int type;
    private Throwable cause;

    public ClientEvent(Client client, int type) {
        this.client = client;
        this.type = type;
    }

    public ClientEvent(Client client, int type, Throwable cause) {
        this.client = client;
        this.type = type;
        this.cause = cause;
    }
    @Override
    public Client getSource() {
        return client;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    @Override
    public String toString() {
        if (cause != null){
            cause.printStackTrace();
        }
        return "type=" + type ;
    }
}
