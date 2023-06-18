package com.feige.fim.adapter;


import com.feige.fim.codec.AbstractNettyCodec;
import com.feige.fim.codec.Codec;
import com.feige.fim.lg.Loggers;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;

import java.util.List;

/**
 * @author feige<br />
 * @ClassName: NettyCodecAdapter <br/>
 * @Description: <br/>
 * @date: 2022/8/13 16:10<br/>
 */
public class NettyCodecAdapter {

    private static final Logger LOG = Loggers.CODEC;

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

    public ChannelHandler getWsDecoder() {
        return new InternalWsDecoder();
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
    private class InternalWsDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {
        
        @Override
        protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame msg, List<Object> out) throws Exception {
            Codec codec = getCodec();
            ByteBuf in = msg.content();
            if (codec instanceof AbstractNettyCodec) {
                ((AbstractNettyCodec) codec).decode(ctx, in, out);
            }
        }
    }
    
    
}
