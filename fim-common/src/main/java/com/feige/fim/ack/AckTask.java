package com.feige.fim.ack;

import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;

public class AckTask extends AbsTask<Packet> {
    public AckTask(Packet packet, AckCallback<Packet> callback, Session session) {
        super(packet, callback, session);
    }

    @Override
    public AbsTask<Packet> copy() {
        AckTask ackTask = new AckTask(this.packet, this.callback, this.session);
        ackTask.setRetryCnt(retryCnt);
        ackTask.setSendTime(sendTime);
        ackTask.setAckManager(ackManager);
        ackTask.setDelay(delay);
        return ackTask;
    }

    @Override
    public Integer getId() {
        return packet.getSeqId();
    }
}
