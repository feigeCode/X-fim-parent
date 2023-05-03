package com.feige.fim.adapter;

import com.feige.api.codec.Codec;
import com.feige.fim.spi.SpiLoader;
import com.feige.fim.spi.SpiNotFoundException;
import org.slf4j.Logger;
import com.feige.fim.lg.Loggers;
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

    private static final Logger LOG = Loggers.CODEC;

    private final ChannelHandler encoder = new InternalEncoder();
    private final ChannelHandler decoder = new InternalDecoder();

    private static Codec codec;
    
    public ChannelHandler getEncoder(){
        return encoder;
    }


    public ChannelHandler getDecoder(){
        return decoder;
    }

    public static Codec getCodec() throws SpiNotFoundException {
        if (codec == null){
            synchronized (NettyCodecAdapter.class){
                if (codec == null) {
                    codec = SpiLoader.getInstance().getSpiByConfigOrPrimary(Codec.class);
                }
            }
        }
        return codec;
    }
    

    private static class InternalEncoder extends MessageToByteEncoder<Object> {

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            Codec codec = getCodec();
        }
    }

    private static class InternalDecoder extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            Codec codec = getCodec();
        }
    }
    
}
