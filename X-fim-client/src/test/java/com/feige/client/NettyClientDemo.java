package com.feige.client;

import com.feige.fim.codec.PacketCodec;
import com.feige.fim.listener.DefaultServerStatusListener;
import com.feige.fim.netty.NettyClient;


import java.net.InetSocketAddress;

public class NettyClientDemo {
    public static void main(String[] args) {
        new NettyClient(new PacketCodec(65536, (byte) -33, (byte) 1, 10, null), System.out::println)
                .connect(new InetSocketAddress("127.0.0.1", 8001), DefaultServerStatusListener.getInstance());
    }
}
