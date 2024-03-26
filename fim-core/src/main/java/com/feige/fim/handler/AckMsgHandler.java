package com.feige.fim.handler;

import com.feige.api.constant.Command;
import com.feige.api.handler.MsgHandler;
import com.feige.api.msg.Ack;
import com.feige.fim.protocol.Packet;
import com.feige.utils.spi.annotation.SPI;

@SPI(value = "ack", interfaces = MsgHandler.class)
public class AckMsgHandler extends ForwardMsgHandler {

    @Override
    public String getReceiverId(Packet packet) {
        Ack ack = this.getMsg(packet, Ack.class);
        return ack.getReceiverId();
    }

    @Override
    public byte getCmd() {
        return Command.ACK.getCmd();
    }
}
