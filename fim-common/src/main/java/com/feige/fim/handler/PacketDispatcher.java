package com.feige.fim.handler;

import com.feige.api.handler.AbstractMsgDispatcher;
import com.feige.api.handler.MsgDispatcher;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;
import com.feige.framework.annotation.InitMethod;
import com.feige.utils.spi.annotation.SPI;
import lombok.Setter;

import java.util.List;


@Setter
@SPI(interfaces = MsgDispatcher.class)
public class PacketDispatcher extends AbstractMsgDispatcher<Packet> {

    private List<MsgHandler> msgHandlers;

    @InitMethod
    public void init(){
        for (MsgHandler msgHandler : msgHandlers) {
            this.register(msgHandler);
        }
    }

    @Override
    public void dispatch(Session session, Packet packet) throws RemotingException {
        byte cmd = packet.getCmd();
        MsgHandler msgHandler = this.getMsgHandler(cmd);
        if (msgHandler != null){
            msgHandler.handle(session, packet);
            return;
        }
        throw new RemotingException(session, "cmd=["+ cmd + "] not found handler");
    }
}
