package com.feige.fim.session;

import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.api.cipher.Cipher;
import com.feige.fim.protocol.Packet;
import com.feige.fim.spi.SpiLoaderUtils;

public class SingleSessionHandler extends AbstractSessionHandler {
    
    @Override
    public String getKey() {
        return "single";
    }

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
            MsgHandler msgHandler = SpiLoaderUtils.get(cmd, MsgHandler.class);
            msgHandler.handle(session, message);
        }
    }
}
