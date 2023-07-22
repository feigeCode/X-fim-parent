package com.feige.fim.handler;

import com.feige.api.annotation.SpiComp;
import com.feige.api.handler.RemotingException;
import com.feige.fim.protocol.Command;
import com.feige.api.handler.AbstractMsgHandler;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;

/**
 * @author feige<br />
 * @ClassName: HandshakeMsgHandler <br/>
 * @Description: <br/>
 * @date: 2023/5/25 21:52<br/>
 */
@SpiComp("handshake")
public class HandshakeMsgHandler extends AbstractMsgHandler<Packet> {
    @Override
    public byte getCmd() {
        return Command.HANDSHAKE.getCmd();
    }

    @Override
    public void handle(Session session, Packet packet) throws RemotingException {
        
    }
    
}
