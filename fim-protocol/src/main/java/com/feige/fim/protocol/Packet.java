package com.feige.fim.protocol;

import com.feige.api.constant.Command;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Setter
@Getter
public class Packet {
    
    /**
     * protocol version
     */
    private byte version;
    /**
     * command
     */
    private byte cmd;
    /**
     * sequence id
     */
    private int seqId;
    /**
     * enabled features
     */
    private byte feats;
    /**
     * checksum
     */
    private short cs;
    /**
     * serializer type 
     */
    private byte serializer;
    /**
     * data origin key
     */
    private byte realType;
    /**
     * data
     */
    private byte[] data;

    public static Packet create(Command cmd) {
        Packet packet = new Packet();
        packet.setCmd(cmd.getCmd());
        return packet;
    }

    public void addFeature(byte feature){
        this.feats |= feature;
    }

    public boolean hasFeature(byte feature){
        return (this.feats & feature) != 0;
    }


    public int getDataLength(){
        return this.getData().length;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "version=" + version +
                ", cmd=" + cmd +
                ", seqId=" + seqId +
                ", feats=" + feats +
                ", cs=" + cs +
                ", realType=" + realType +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
