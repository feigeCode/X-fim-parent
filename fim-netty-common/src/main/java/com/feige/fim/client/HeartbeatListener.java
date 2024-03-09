package com.feige.fim.client;

import com.feige.api.constant.Command;
import com.feige.api.session.Session;
import com.feige.fim.event.NettyEventTrigger;
import com.feige.fim.protocol.Packet;
import com.feige.utils.event.Async;
import com.feige.utils.event.EventListener;
import com.feige.utils.spi.annotation.SPI;
import com.google.common.eventbus.Subscribe;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@SPI(value = "clientHeartbeatListener",interfaces = EventListener.class)
public class HeartbeatListener implements EventListener {



    @Subscribe
    @Async
    public void trigger(NettyEventTrigger trigger) throws Exception {
        Object evt = trigger.getEvent();
        Session session = trigger.getSession();
        if (evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE){
                // TODO 事件
            }
            
            if (state == IdleState.WRITER_IDLE){
                if (session.isConnected()) {
                    Packet packet = Packet.create(Command.HEARTBEAT);
                    session.write(packet);
                }
            }

        }
    }



}
