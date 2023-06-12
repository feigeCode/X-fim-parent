package com.feige.fim.protocol;

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
     * sequence number
     */
    private int sequenceNum;
    /**
     * enabled features
     */
    private byte features;
    /**
     * checksum
     */
    private short cs;
    /**
     * data origin key
     */
    private byte classKey;
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
        this.features |= feature;
    }

    public boolean hasFeature(byte feature){
        return (this.features & feature) != 0;
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

    public int getSequenceNum() {
        return sequenceNum;
    }

    public void setSequenceNum(int sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    

    public byte getFeatures() {
        return features;
    }

    public void setFeatures(byte features) {
        this.features = features;
    }

    public short getCs() {
        return cs;
    }

    public void setCs(short cs) {
        this.cs = cs;
    }

    public byte getClassKey() {
        return classKey;
    }

    public void setClassKey(byte classKey) {
        this.classKey = classKey;
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
}
