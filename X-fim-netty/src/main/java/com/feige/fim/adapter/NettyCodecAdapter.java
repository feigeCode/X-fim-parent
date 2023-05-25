package com.feige.fim.adapter;

import com.feige.api.codec.Codec;
import com.feige.api.codec.IByteBuf;
import com.feige.api.session.Session;
import com.feige.api.session.SessionRepository;
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

    private final ChannelHandler encoder = new InternalEncoder();
    private final ChannelHandler decoder = new InternalDecoder();
    private final ChannelHandler wsDecoder = new InternalWsDecoder();

    private final Codec codec;
    private final SessionRepository sessionRepository;

    public NettyCodecAdapter(Codec codec, SessionRepository sessionRepository) {
        this.codec = codec;
        this.sessionRepository = sessionRepository;
    }

    public ChannelHandler getEncoder(){
        return encoder;
    }


    public ChannelHandler getDecoder(){
        return decoder;
    }

    public ChannelHandler getWsDecoder() {
        return wsDecoder;
    }

    public Codec getCodec() {
        return codec;
    }

    public SessionRepository getSessionRepository() {
        return sessionRepository;
    }

    private class InternalEncoder extends MessageToByteEncoder<Object> {

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            Codec codec = getCodec();
            final IByteBuf byteBuf = NettyByteBufAdapter.fromByteBuf(out);
            codec.encode(toSession(ctx),byteBuf , msg);
        }
    }

    private class InternalDecoder extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            Codec codec = getCodec();
            Object obj = codec.decode(toSession(ctx), NettyByteBufAdapter.fromByteBuf(in));
            out.add(obj);
        }
    }

    protected Session toSession(ChannelHandlerContext ctx){
        return NettySession.getOrAddSession(ctx, getSessionRepository());
    }

    
    private class InternalWsDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {
        
        @Override
        protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame msg, List<Object> out) throws Exception {
            Codec codec = getCodec();
            final ByteBuf in = msg.content();
            Object obj = codec.decode(toSession(ctx), NettyByteBufAdapter.fromByteBuf(in));
            out.add(obj);
        }
    }
    
    
}
