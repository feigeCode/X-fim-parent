package com.feige.fim.ack;

import com.feige.api.codec.PacketInterceptor;
import com.feige.api.constant.ProtocolConst;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;
import com.feige.framework.annotation.Inject;
import com.feige.utils.spi.annotation.SPI;


@SPI(interfaces = PacketInterceptor.class)
public class AckPacketInterceptor implements PacketInterceptor {

    @Inject
    private AckManager ackManager;

    @Override
    public void writePacket(Session session, Object packet) {
        autoAck(session, packet);
    }

    private void autoAck(Session session, Object obj){
        Packet packet = (Packet) obj;
        if(packet.hasFeature(ProtocolConst.AUTO_ACK)){
            AckTask ackTask = AckTask.create(packet, new DefaultAckCallback(), session);
            ackManager.addTask(ackTask);
        }
    }

    @Override
    public void readPacket(Session session, Object packet) {

    }

    @Override
    public int order() {
        return 0;
    }
}
