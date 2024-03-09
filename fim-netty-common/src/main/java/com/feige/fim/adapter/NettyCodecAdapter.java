package com.feige.fim.adapter;


import com.feige.api.codec.Codec;
import com.feige.api.codec.DecoderException;
import com.feige.api.codec.EncoderException;
import com.feige.fim.factory.NettySessionFactory;
import com.feige.utils.logger.Loggers;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
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

    public NettyCodecAdapter(Codec  codec) {
        this.codec = codec;
    }

    public Codec  getCodec() {
        return codec;
    }

    public ChannelHandler getTcpCodec(){
        return new InternalCodec();
    }

    public ChannelHandler getWsCodec(){
        return new InternalWsCodec();
    }
    private class InternalCodec extends MessageToMessageCodec<Object, Object> {

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
            try {
                Codec codec = getCodec();
                codec.encode(NettySessionFactory.getOrAddSession(ctx), msg, out);
            }catch (EncoderException e){
                ctx.channel().close();
                throw e;
            }
        }

        @Override
        protected void decode(ChannelHandlerContext ctx, Object in, List<Object> out) throws Exception {
            try {
                Codec codec = getCodec();
                codec.decode(NettySessionFactory.getOrAddSession(ctx), in, out);
            }catch (DecoderException e){
                ctx.channel().close();
                throw e;
            }
        }
    }

    private class InternalWsCodec extends MessageToMessageCodec<BinaryWebSocketFrame, Object> {

        protected ByteBuf allocateBuffer(ChannelHandlerContext ctx, Object msg, boolean preferDirect) throws Exception {
            return preferDirect ? ctx.alloc().ioBuffer() : ctx.alloc().heapBuffer();
        }

        @Override
        public boolean acceptOutboundMessage(Object msg) throws Exception {
            if (msg instanceof FullHttpResponse){
                return false;
            }
            return super.acceptOutboundMessage(msg);
        }

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> list) throws Exception {
            try {
                // 申请buffer
                ByteBuf out = this.allocateBuffer(ctx, msg, true);
                Codec codec = getCodec();
                codec.encode(NettySessionFactory.getOrAddSession(ctx), msg, out);
                list.add(new BinaryWebSocketFrame(out));
            }catch (EncoderException e){
                ctx.channel().close();
                throw e;
            }
        }

        @Override
        protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame msg, List<Object> out) throws Exception {
            try {
                Codec codec = getCodec();
                ByteBuf in = msg.content();
                codec.decode(NettySessionFactory.getOrAddSession(ctx), in, out);
            }catch (DecoderException e){
                ctx.channel().close();
                throw e;
            }
        }
    }
}
