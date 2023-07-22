package com.feige.client;

import com.feige.fim.codec.PacketCodec;
import com.feige.fim.handler.ClientSessionHandler;
import com.feige.fim.handler.PacketDispatcher;
import com.feige.fim.netty.NettyClient;

import java.net.InetSocketAddress;

public class NettyClientDemo {
    public static void main(String[] args) {
        PacketDispatcher packetDispatcher = new PacketDispatcher();
        ClientSessionHandler clientSessionHandler = new ClientSessionHandler(packetDispatcher);
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8001);
        PacketCodec packetCodec = new PacketCodec(65536, (byte) -33, (byte) 1, 10, null);
        new NettyClient(address, packetCodec, clientSessionHandler).syncStart();
    }
}
