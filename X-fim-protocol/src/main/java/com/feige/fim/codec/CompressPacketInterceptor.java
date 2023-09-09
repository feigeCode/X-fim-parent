package com.feige.fim.codec;

import com.feige.api.codec.PacketInterceptor;
import com.feige.api.constant.ProtocolConst;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;
import com.feige.framework.utils.Configs;
import com.feige.utils.common.IOUtils;
import com.feige.utils.spi.annotation.SpiComp;


@SpiComp(interfaces = PacketInterceptor.class)
public class CompressPacketInterceptor implements PacketInterceptor {
    @Override
    public void writePacket(Session session, Object packet) {
        this.compress(packet, session);
    }

    private void compress(Object obj, Session session){
        Packet packet = (Packet) obj;
        if (ProtocolConst.SerializedClass.isCustomCrypto(packet.getClassKey())) {
            return;
        }
        Boolean enable = Configs.getBoolean(Configs.ConfigKey.COMPRESS_ENABLE, false);
        if (enable){
            byte[] data = packet.getData();
            byte[] compressData = IOUtils.compress(data);
            packet.setData(compressData);
            packet.addFeature(ProtocolConst.COMPRESS);
        }
    }
    
    @Override
    public void readPacket(Session session, Object packet) {
        this.decompress(packet, session);
    }

    private void decompress(Object obj, Session session){
        Packet packet = (Packet) obj;
        if (packet.hasFeature(ProtocolConst.COMPRESS)) {
            packet.setData(IOUtils.decompress(packet.getData()));
        }
    }

    @Override
    public int order() {
        return 1;
    }
}
