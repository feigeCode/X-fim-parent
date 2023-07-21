package com.feige.fim.session;

import com.feige.api.annotation.SpiComp;
import com.feige.api.handler.AbstractSessionHandler;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.api.cipher.Cipher;
import com.feige.fim.protocol.Packet;
import com.feige.fim.context.AppContext;

@SpiComp("single")
public class SingleSessionHandler extends AbstractSessionHandler {
    

    @Override
    public void received(Session session, Object message) throws RemotingException {
        if (message instanceof Packet){
            Packet packet = (Packet) message;
            Cipher cipher = session.getCipher();
            if (cipher != null){
                byte[] data = packet.getData();
                byte[] decryptData = cipher.decrypt(data);
                packet.setData(decryptData);
            }
            String cmd = String.valueOf(packet.getCmd());
            MsgHandler msgHandler = AppContext.get(cmd, MsgHandler.class);
            msgHandler.handle(session, message);
        }
    }
}
