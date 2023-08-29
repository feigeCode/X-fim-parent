package com.feige.fim.codec;

import com.feige.utils.spi.annotation.SpiComp;
import com.feige.framework.annotation.Value;
import com.feige.api.codec.Codec;
import com.feige.framework.api.spi.SpiCompProvider;
import com.feige.framework.utils.Configs.ConfigKey;


@SpiComp(value="packet", interfaces = SpiCompProvider.class, provideTypes = Codec.class)
public class PacketCodecSpiCompProvider implements SpiCompProvider<Codec> {
    
    @Value(ConfigKey.CODEC_MAX_PACKET_SIZE_KEY)
    private int maxPacketSize;
    @Value(ConfigKey.CODEC_HEARTBEAT_KEY)
    private byte heartbeat;
    @Value(ConfigKey.CODEC_VERSION_KEY)
    private byte version;
    @Value(ConfigKey.CODEC_HEADER_LENGTH_KEY)
    private int headerLength;
    @Value(value = ConfigKey.CODEC_CHECK_SUM_KEY, nullSafe = false)
    private String checkSumKey;
    
    @Override
    public PacketCodec getInstance() {
        return new PacketCodec(this.maxPacketSize, this.heartbeat, this.version, this.headerLength, this.checkSumKey);
    }
    
}