package com.feige.fim.netty;


import com.feige.fim.codec.Codec;
import com.feige.fim.codec.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

/**
 * @author feige<br />
 * @ClassName: NettyCodecAdapter <br/>
 * @Description: <br/>
 * @date: 2022/8/13 16:10<br/>
 */
public class NettyCodecAdapter {


    private final ChannelHandler encoder = new InternalEncoder();
    private final ChannelHandler decoder = new InternalDecoder();

    private final Codec codec;

    public NettyCodecAdapter(Codec  codec) {
        this.codec = codec;
    }

    public ChannelHandler getEncoder(){
        return encoder;
    }


    public ChannelHandler getDecoder(){
        return decoder;
    }


    public Codec  getCodec() {
        return codec;
    }
    

    private class InternalEncoder extends MessageToByteEncoder<Object> {

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            Codec codec = getCodec();
            if (codec instanceof PacketCodec) {
                ((PacketCodec) codec).encode(ctx, msg, out);
            }
        }
    }

    private class InternalDecoder extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            Codec codec = getCodec();
            if (codec instanceof PacketCodec) {
                ((PacketCodec) codec).decode(ctx, in, out);
            }
        }
    }
    
}
