package com.feige.fim.server.udp;

import com.feige.fim.sc.Server;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.DatagramChannel;


public class UdpServerInitializer extends ChannelInitializer<DatagramChannel> {

    public UdpServerInitializer(Server server) {
        
    }

    @Override
    protected void initChannel(DatagramChannel datagramChannel) throws Exception {
        ChannelPipeline pipeline = datagramChannel.pipeline();
    }
}
