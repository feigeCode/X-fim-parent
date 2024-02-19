package com.feige.fim.codec;

import com.feige.api.codec.ICheckSum;
import com.feige.api.codec.PacketInterceptor;
import com.feige.framework.aware.ApplicationContextAware;
import com.feige.framework.context.api.ApplicationContext;
import com.feige.framework.utils.Configs;
import com.feige.utils.common.StringUtils;
import com.feige.utils.spi.annotation.SPI;
import com.feige.framework.annotation.Value;
import com.feige.api.codec.Codec;
import com.feige.framework.spi.api.SpiCompProvider;
import com.feige.framework.utils.Configs.ConfigKey;

import java.util.List;


@SPI(value="packet", interfaces = SpiCompProvider.class, provideTypes = Codec.class)
public class PacketCodecSpiCompProvider implements SpiCompProvider<Codec>, ApplicationContextAware {
    
    @Value(ConfigKey.CODEC_MAX_PACKET_SIZE_KEY)
    private int maxPacketSize;
    @Value(ConfigKey.CODEC_HEARTBEAT_KEY)
    private byte heartbeat;
    @Value(ConfigKey.CODEC_VERSION_KEY)
    private byte version;
    @Value(ConfigKey.CODEC_HEADER_LENGTH_KEY)
    private int headerLength;
    
    private ApplicationContext applicationContext;
    
    @Override
    public PacketCodec getInstance() {
        ICheckSum checkSum = null;
        String checkSumKey = applicationContext.getEnvironment().getString(Configs.ConfigKey.CODEC_CHECK_SUM_KEY);
        if (StringUtils.isNotBlank(checkSumKey)){
            checkSum = applicationContext.get(checkSumKey, ICheckSum.class);
        }
        List<PacketInterceptor> packetInterceptors = applicationContext.getByType(PacketInterceptor.class);
        return new PacketCodec(this.maxPacketSize, this.heartbeat, this.version, this.headerLength, checkSum, packetInterceptors);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
