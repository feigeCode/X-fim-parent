package com.feige.fim.ack;

import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;
import com.feige.utils.common.AssertUtil;

public class AckTask extends AbsTask<Packet> {
    public AckTask(Packet packet, AckCallback<Packet> callback, Session session) {
        super(packet, callback, session);
    }

    public static AckTask create(Packet packet, AckCallback<Packet> callback, Session session){
        AssertUtil.notNull(packet , "packet");
        AssertUtil.notNull(session , "session");
        return new AckTask(packet, callback, session);
    }

    public static AckTask create(Packet packet, Session session){
        return create(packet, null, session);
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
    public int getId() {
        return packet.getSeqId();
    }
}
