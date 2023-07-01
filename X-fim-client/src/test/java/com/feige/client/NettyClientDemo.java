package com.feige.client;

import com.feige.fim.codec.PacketCodec;
import com.feige.fim.listener.DefaultServerStatusListener;
import com.feige.fim.netty.NettyClient;
import com.feige.fim.protocol.Command;
import com.feige.fim.protocol.Packet;
import com.feige.fim.push.PushManager;


import java.net.InetSocketAddress;

public class NettyClientDemo {
    public static void main(String[] args) {
        new NettyClient(new PacketCodec(65536, (byte) -33, (byte) 1, 10, null), System.out::println)
                .connect(new InetSocketAddress("127.0.0.1", 8001), DefaultServerStatusListener.getInstance());
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
}
