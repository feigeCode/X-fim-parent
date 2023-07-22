package com.feige.fim.handler;

import com.feige.api.handler.MsgDispatcher;
import com.feige.fim.protocol.Packet;

public class ClientSessionHandler extends AbstractSessionHandler {

    public ClientSessionHandler(MsgDispatcher<Packet> msgDispatcher) {
        this.msgDispatcher = msgDispatcher;
    }
}
