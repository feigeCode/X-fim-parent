package com.feige.fim.codec;


import com.feige.fim.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class PacketCodec extends AbstractNettyCodec {


    public PacketCodec(int maxPacketSize, byte heartbeat, byte version, int headerLength, String checkSumKey) {
        super(maxPacketSize, heartbeat, version, headerLength, checkSumKey);
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof Packet) {
            encode(msg, out);
        }
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            Object decode = decode(in);
            if (decode instanceof Packet){
                Packet packet = (Packet) decode;
                out.add(packet);
            }
        } catch (DecoderException e) {
            ctx.channel().close();
            throw e;
        }
    }
}
    