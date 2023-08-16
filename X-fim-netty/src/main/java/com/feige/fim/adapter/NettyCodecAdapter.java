package com.feige.fim.adapter;


import com.feige.api.codec.DecoderException;
import com.feige.api.codec.EncoderException;
import com.feige.api.session.SessionRepository;
import com.feige.api.codec.Codec;
import com.feige.fim.factory.NettySessionFactory;
import com.feige.fim.utils.lg.Loggers;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpResponse;
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
    private final SessionRepository sessionRepository;

    public NettyCodecAdapter(Codec  codec, SessionRepository sessionRepository) {
        this.codec = codec;
        this.sessionRepository = sessionRepository;
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
    
    public SessionRepository getSessionRepository(){
        return sessionRepository;
    }

    private class InternalEncoder extends MessageToByteEncoder<Object> {

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
           
            try {
                Codec codec = getCodec();
                codec.encode(NettySessionFactory.getOrAddSession(ctx, getSessionRepository()), msg, out);
            }catch (EncoderException e){
                ctx.channel().close();
                throw e;
            }
        }
        @Override
        public boolean acceptOutboundMessage(Object msg) throws Exception {
            if (msg instanceof FullHttpResponse){
               return false;
            }
            return super.acceptOutboundMessage(msg);
        } 
    }

    private class InternalDecoder extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            try {
                Codec codec = getCodec();
                codec.decode(NettySessionFactory.getOrAddSession(ctx, getSessionRepository()), in, out);
            }catch (DecoderException e){
                ctx.channel().close();
                throw e;
            }
        }
    }
    private class InternalWsDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {
        
        @Override
        protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame msg, List<Object> out) throws Exception {
           try {
               Codec codec = getCodec();
               ByteBuf in = msg.content();
               codec.decode(NettySessionFactory.getOrAddSession(ctx, getSessionRepository()), in, out);
           }catch (DecoderException e){
               ctx.channel().close();
               throw e;
           }
        }
    }
    
    
}
