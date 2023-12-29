package com.feige.fim.protocol;

import com.feige.api.constant.Command;

import java.util.Arrays;

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


    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }


    public byte getCmd() {
        return cmd;
    }

    public void setCmd(byte cmd) {
        this.cmd = cmd;
    }

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    

    public byte getFeats() {
        return feats;
    }

    public void setFeats(byte feats) {
        this.feats = feats;
    }

    public short getCs() {
        return cs;
    }

    public void setCs(short cs) {
        this.cs = cs;
    }

    public byte getSerializer() {
        return serializer;
    }

    public void setSerializer(byte serializer) {
        this.serializer = serializer;
    }

    public byte getRealType() {
        return realType;
    }

    public void setRealType(byte realType) {
        this.realType = realType;
    }

   

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
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
