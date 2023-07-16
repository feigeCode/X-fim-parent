package com.feige.fim.codec;

import com.feige.api.annotation.Spi;
import com.feige.api.annotation.Value;
import com.feige.api.codec.Codec;
import com.feige.api.spi.InstanceProvider;
import com.feige.fim.config.Configs;

@Spi("packet")
public class PacketCodecInstanceProvider implements InstanceProvider<Codec> {
    
    @Value(Configs.ConfigKey.CODEC_MAX_PACKET_SIZE_KEY)
    private int maxPacketSize;
    @Value(Configs.ConfigKey.CODEC_HEARTBEAT_KEY)
    private byte heartbeat;
    @Value(Configs.ConfigKey.CODEC_VERSION_KEY)
    private byte version;
    @Value(Configs.ConfigKey.CODEC_HEADER_LENGTH_KEY)
    private int headerLength;
    @Value(Configs.ConfigKey.CODEC_CHECK_SUM_KEY)
    private String checkSumKey;
    
    @Override
    public PacketCodec getInstance() {
        return new PacketCodec(this.maxPacketSize, this.heartbeat, this.version, this.headerLength, this.checkSumKey);
    }

    @Override
    public Class<Codec> getType() {
        return Codec.class;
    }
}
