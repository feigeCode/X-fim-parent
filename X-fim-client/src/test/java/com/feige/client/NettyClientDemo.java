package com.feige.client;

import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.fim.codec.PacketCodec;
import com.feige.fim.handler.AbstractSessionHandler;
import com.feige.fim.netty.NettyClient;
import com.feige.fim.protocol.Command;
import com.feige.fim.protocol.Packet;

import java.net.InetSocketAddress;

public class NettyClientDemo {
    public static void main(String[] args) {
        new NettyClient(new InetSocketAddress("127.0.0.1", 8001), new PacketCodec(65536, (byte) -33, (byte) 1, 10, null), new AbstractSessionHandler() {
            @Override
            public void connected(Session session) throws RemotingException {
                super.connected(session);
            }
        }, null).syncStart();
        System.out.println("channel active");
        Packet packet = Packet.create(Command.HANDSHAKE);
        packet.setSequenceNum(1);
        packet.setClassKey((byte)1);
        packet.setFeatures((byte)0);
        byte[] bytes = new byte[2];
        bytes[0] = 1;
        bytes[1] = 2;
        packet.setData(bytes);
        
    }
}
