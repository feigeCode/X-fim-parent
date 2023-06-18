package com.feige.client;

import com.feige.fim.codec.PacketCodec;
import com.feige.fim.listener.ChannelActiveListener;
import com.feige.fim.listener.DefaultServerStatusListener;
import com.feige.fim.netty.NettyClient;
import com.feige.fim.utils.EventDispatcher;


import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class NettyClientDemo {
    public static void main(String[] args) {
        EventDispatcher.create(Executors.newSingleThreadExecutor());
        EventDispatcher.register(new ChannelActiveListener());
        new NettyClient(new PacketCodec(65536, (byte) -33, (byte) 1, 10, "default"), System.out::println)
                .connect(new InetSocketAddress("127.0.0.1", 8001), DefaultServerStatusListener.getInstance());
    }
}
