package com.feige.fim.ack;

import com.feige.fim.protocol.Packet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultAckCallback implements AckCallback<Packet>  {
    @Override
    public void onSuccess(Packet packet) {
        System.out.println("ack success ,seqId=" + packet.getSeqId());
    }

    @Override
    public void onTimeout(Packet packet) {
        System.out.println("ack timeout ,seqId=" + packet.getSeqId());

    }

    @Override
    public void onFailure(Packet packet, Throwable cause) {
        log.error("ack error ,seqId={}",packet.getSeqId(), cause);
    }
}
