package com.feige.fim.server;

import com.feige.api.session.Session;
import com.feige.fim.event.NettyEventTrigger;
import com.feige.utils.event.EventListener;
import com.feige.utils.logger.Loggers;
import com.feige.utils.spi.annotation.SPI;
import com.google.common.eventbus.Subscribe;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;


@SPI(value = "serverHeartbeatListener",interfaces = EventListener.class)
public class HeartbeatListener implements EventListener {
    private final static String HEARTBEAT_CNT = "hb_cnt";
    private final static int MAX_TIMEOUT_CNT = 3;

    @Subscribe
    public void trigger(NettyEventTrigger eventTrigger){
        Object event = eventTrigger.getEvent();
        Session session = eventTrigger.getSession();
        if (event instanceof IdleStateEvent){
            final IdleStateEvent idleStateEvent = (IdleStateEvent) event;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                Integer hbCnt = (Integer) session.getAttr(HEARTBEAT_CNT);
                if (hbCnt == null){
                    hbCnt = 1;
                    if (!session.isHandshake()){
                        Loggers.HEARTBEAT.warn("Close the session of the not handshake, id = {}, remote address = {}", session.getId(), session.getRemoteAddress());
                        session.close();
                        return;
                    }
                }else {

                    if (hbCnt == 2){
                        if (!session.isBind()){
                            Loggers.HEARTBEAT.warn("Close the session of the unbound client, id = {}, remote address = {}", session.getId(), session.getRemoteAddress());
                            session.close();
                            return;
                        }
                    }

                    if (hbCnt > MAX_TIMEOUT_CNT){
                        session.close();
                        Loggers.HEARTBEAT.warn("The session is closed because the client heartbeat is not received for a long time. Procedure, id = {}, remote address = {}", session.getId(), session.getRemoteAddress());
                        return;
                    }
                    hbCnt++;
                }
                session.setAttr(HEARTBEAT_CNT, hbCnt);
                Loggers.HEARTBEAT.debug("read idle, id = {}, remote address = {}, heartbeat count = {}", session.getId(), session.getRemoteAddress(), hbCnt);
            }
            if (idleStateEvent.state() == IdleState.WRITER_IDLE){
                session.close();
                Loggers.HEARTBEAT.warn("No message was written to the session for 60 seconds. Closed, id = {}, remote address = {}", session.getId(), session.getRemoteAddress());
            }
        }
    }
}
