package com.feige.fim.handler;

import com.feige.api.constant.Command;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.msg.Ack;
import com.feige.api.session.Session;
import com.feige.fim.ack.AckManager;
import com.feige.fim.ack.AckTask;
import com.feige.fim.protocol.Packet;
import lombok.Setter;
import com.feige.utils.spi.annotation.SPI;


@SPI(interfaces = MsgHandler.class)
public class AckMsgHandler extends AbstractMsgHandler{

    @Setter
    private AckManager ackManager;
    @Override
    public byte getCmd() {
        return Command.ACK.getCmd();
    }

    @Override
    public void handle(Session session, Packet msg) throws RemotingException {
        Ack ackMsg = this.getMsg(msg, Ack.class);
        int seqId = ackMsg.getSeqId();
        AckTask task = (AckTask) ackManager.getAndRemove(seqId);
        if (task != null){
            task.ack(msg);
        }
    }
}
