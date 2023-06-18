package com.feige.fim.netty;


import com.feige.fim.codec.AbstractNettyCodec;
import com.feige.fim.codec.Codec;
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



    private final Codec codec;

    public NettyCodecAdapter(Codec  codec) {
        this.codec = codec;
    }

    public ChannelHandler getEncoder(){
        return new InternalEncoder();
    }


    public ChannelHandler getDecoder(){
        return new InternalDecoder();
    }


    public Codec  getCodec() {
        return codec;
    }
    

    private class InternalEncoder extends MessageToByteEncoder<Object> {

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            Codec codec = getCodec();
            if (codec instanceof AbstractNettyCodec) {
                ((AbstractNettyCodec) codec).encode(ctx, msg, out);
            }
        }
    }

    private class InternalDecoder extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            Codec codec = getCodec();
            if (codec instanceof AbstractNettyCodec) {
                ((AbstractNettyCodec) codec).decode(ctx, in, out);
            }
        }
    }
    
}
