package com.feige.fim.netty;


import com.feige.api.codec.Codec;
import com.feige.api.codec.DecoderException;
import com.feige.api.codec.EncoderException;
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
    
    private final NettyClient nettyClient;
    

    public NettyCodecAdapter(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    public ChannelHandler getEncoder(){
        return new InternalEncoder();
    }


    public ChannelHandler getDecoder(){
        return new InternalDecoder();
    }


    public Codec  getCodec() {
        return nettyClient.getCodec();
    }
    

    private class InternalEncoder extends MessageToByteEncoder<Object> {

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            try {
                Codec codec = getCodec();
                codec.encode(nettyClient.getSession(), msg, out);
            } catch (EncoderException e) {
                ctx.channel().close();
                throw e;
            }
            
        }
    }

    private class InternalDecoder extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            try {
                Codec codec = getCodec();
                codec.decode(nettyClient.getSession(), in, out);
            } catch (DecoderException e) {
                ctx.channel().close();
                throw e;
            }
        }
    }
    
}
