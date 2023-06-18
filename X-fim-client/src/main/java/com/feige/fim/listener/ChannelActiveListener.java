package com.feige.fim.listener;

import com.feige.fim.api.Client;
import com.feige.fim.event.ChannelActive;
import com.google.common.eventbus.Subscribe;

public class ChannelActiveListener {
 
    
    @Subscribe
    public void handleEvent(ChannelActive channelActive){
        int type = channelActive.getType();
        Client client = channelActive.getSource();
        if (ChannelActive.CHANNEL_INACTIVE == type){
            client.reconnect(DefaultServerStatusListener.getInstance());
        }
    }
}
