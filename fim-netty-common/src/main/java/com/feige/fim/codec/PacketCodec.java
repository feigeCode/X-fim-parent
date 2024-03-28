package com.feige.fim.codec;

import com.feige.api.codec.Codec;
import com.feige.api.codec.DecoderException;
import com.feige.api.codec.EncoderException;
import com.feige.api.codec.ICheckSum;
import com.feige.api.codec.PacketInterceptor;
import com.feige.api.codec.VersionException;
import com.feige.api.constant.Command;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;
import com.feige.framework.annotation.DisableInject;
import com.feige.framework.annotation.InitMethod;
import com.feige.framework.annotation.Value;
import com.feige.framework.aware.ApplicationContextAware;
import com.feige.framework.context.api.ApplicationContext;
import com.feige.framework.utils.Configs;
import com.feige.utils.common.StringUtils;
import com.feige.utils.logger.Loggers;
import com.feige.utils.spi.annotation.SPI;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.TooLongFrameException;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;


@SPI(value="packet", interfaces = Codec.class)
@Getter
public class PacketCodec implements Codec , ApplicationContextAware {
    @Value(Configs.ConfigKey.CODEC_MAX_PACKET_SIZE_KEY)
    private int maxPacketSize;
    @Value(Configs.ConfigKey.CODEC_HEARTBEAT_KEY)
    private byte heartbeat;
    @Value(Configs.ConfigKey.CODEC_VERSION_KEY)
    private byte version;
    @Value(Configs.ConfigKey.CODEC_HEADER_LENGTH_KEY)
    private int headerLength;
    @DisableInject
    private  ICheckSum checkSum;

    @Setter
    private  List<PacketInterceptor> packetInterceptors;

    private PacketHandler packetHandler;
    @Setter
    private ApplicationContext applicationContext;

    @InitMethod
    public void init(){
        String checkSumKey = applicationContext.getEnvironment().getString(Configs.ConfigKey.CODEC_CHECK_SUM_KEY);
        if (StringUtils.isNotBlank(checkSumKey)){
           checkSum = applicationContext.get(checkSumKey, ICheckSum.class);
        }
        packetInterceptors.sort(Comparator.comparingInt(PacketInterceptor::order));
        packetHandler = new PacketHandler(packetInterceptors);
    }

    

    @Override
    public void encode(Session session ,Object msg, Object b) throws EncoderException {
        if (!(msg instanceof Packet)){
            return;
        }
        Packet packet = (Packet) msg;
        ByteBuf byteBuf = (ByteBuf) b;
        Loggers.CONSOLE.info(ByteBufUtil.prettyHexDump(byteBuf));
        if (packet.getCmd() == Command.HEARTBEAT.getCmd()){
            byteBuf.writeByte(getHeartbeat());
        }else {
            // 包拦截器，对包做一些处理
            packetHandler.writePacket(session, packet);
            int dataLength = packet.getDataLength();
            byteBuf.writeByte(getVersion());
            byteBuf.writeInt(dataLength);
            byteBuf.writeByte(packet.getCmd());
            byteBuf.writeInt(packet.getSeqId());
            byteBuf.writeByte(packet.getFeats());
            byteBuf.writeShort(packet.getCs());
            byteBuf.writeByte(packet.getSerializer());
            byteBuf.writeByte(packet.getRealType());
            if (dataLength > 0){
                byteBuf.writeBytes(packet.getData());
            }
        }
    }

    @Override
    public void decode(Session session, Object b, List<Object> out) throws DecoderException {
        if (!(b instanceof ByteBuf)){
            return;
        }
        ByteBuf byteBuf = (ByteBuf) b;
        byteBuf.markReaderIndex();
        if (byteBuf.isReadable()) {
            if (byteBuf.readByte() == getHeartbeat()) {
                Packet packet = Packet.create(Command.HEARTBEAT);
                packet.setVersion(getVersion());
                out.add(packet);
            } else {
                byteBuf.resetReaderIndex();
                byte version = checkVersion(byteBuf);
                int bodyLength = checkLength(byteBuf);
                if (bodyLength != -1) {
                    byte cmd = byteBuf.readByte();
                    int seqId = byteBuf.readInt();
                    byte feats = byteBuf.readByte();
                    short checksum = checkChecksum(byteBuf);
                    byte serializer = byteBuf.readByte();
                    byte realType = byteBuf.readByte();
                    byte[] data = new byte[bodyLength];
                    byteBuf.readBytes(data);
                    Packet packet = Packet.create(Command.valueOf(cmd));
                    packet.setSeqId(seqId);
                    packet.setVersion(version);
                    packet.setFeats(feats);
                    packet.setCs(checksum);
                    packet.setSerializer(serializer);
                    packet.setRealType(realType);
                    packet.setData(data);
                    // 包拦截器，对包做一些处理
                    packetHandler.readPacket(session, packet);
                    out.add(packet);
                }else {
                    byteBuf.resetReaderIndex();
                }
            }
        }
        Loggers.CONSOLE.info(out.toString());
    }
    
    

    private byte checkVersion(ByteBuf byteBuf) {
        byte version = byteBuf.readByte();
        if (version != getVersion()) {
            throw new VersionException("The protocol version does not match");
        }
        return version;
    }

    private short checkChecksum(ByteBuf byteBuf){
        short cs = byteBuf.readShort();
        if (isNeedCheckSum()){
            byte[] data = byteBuf.array();
            checkSum.check(data, cs);
        }
        return cs;
    }

    protected boolean isNeedCheckSum(){
        return checkSum != null;
    }

    private int checkLength(ByteBuf byteBuf) {
        int bodyLength = byteBuf.readInt();
        if (bodyLength < 0){
            throw new DecoderException("negative length: " + bodyLength);
        }
        if (bodyLength > getMaxPacketSize()) {
            throw new TooLongFrameException("packet body length over limit:" + bodyLength);
        }
        int readableBytes = byteBuf.readableBytes();
        return bodyLength + (getHeaderLength() - 4 - 1) <= readableBytes ? bodyLength : -1;
    }
}
