package com.feige.fim.listener;

import com.feige.fim.api.Client;
import com.feige.fim.event.ChannelActive;
import com.feige.fim.protocol.Command;
import com.feige.fim.protocol.Packet;
import com.feige.fim.push.PushManager;

public class ChannelActiveListener {

    private static ChannelActiveListener channelActiveListener;

    public static ChannelActiveListener getInstance(){
        if (channelActiveListener == null){
            synchronized (ChannelActiveListener.class){
                if (channelActiveListener == null) {
                    channelActiveListener = new ChannelActiveListener();
                }
            }
        }
        return channelActiveListener;
    }

    private ChannelActiveListener(){

    }
    
    public void handleEvent(ChannelActive channelActive){
        int type = channelActive.getType();
        Client client = channelActive.getSource();
        if (ChannelActive.CHANNEL_ACTIVE == type){
            PushManager.getInstance().setPushService(client.getPushService());
            System.out.println("channel active");
            Packet packet = Packet.create(Command.HANDSHAKE);
            packet.setSequenceNum(1);
            packet.setClassKey((byte)1);
            packet.setFeatures((byte)0);
            byte[] bytes = new byte[2];
            bytes[0] = 1;
            bytes[1] = 2;
            packet.setData(bytes);
            PushManager.push(packet);
        }
        if (ChannelActive.CHANNEL_INACTIVE == type){
//            client.reconnect(DefaultServerStatusListener.getInstance());
            System.out.println("channel inactive");
        }
    }
}
