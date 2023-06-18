package com.feige.fim.event;

import com.feige.fim.api.Client;
import com.feige.fim.api.Event;

public class ChannelActive implements Event<Client> {
    public static final int CHANNEL_ACTIVE = 1;
    public static final int CHANNEL_INACTIVE = 2;
    private final Client client;
    private final int type;
    private Throwable cause;

    public ChannelActive(Client client, int type) {
        this.client = client;
        this.type = type;
    }

    public ChannelActive(Client client, int type, Throwable cause) {
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
        return "type=" + type +
                ", cause=" + cause;
    }
}
