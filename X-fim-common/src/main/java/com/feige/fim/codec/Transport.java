package com.feige.fim.codec;


/**
 * bodyLength       int类型       消息体长度
 * cs               byte类型      校验和
 * version          byte类型      协议版本
 * cmd              byte类型      操作命令
 * serializeType    byte类型      序列化类型
 * srcId            byte[]类型    源ID
 * destId           byte[]类型    目标ID
 * features         byte类型      启用特性
 * body             byte[]类型    数据
 */
public class Transport {


    /**
     * 协议版本
     */
    private byte version;
    /**
     * 校验和
     */
    private byte cs;
    /**
     * 操作命令
     */
    private byte cmd;
    /**
     * 序列化类型
     */
    private byte serializeType;
    /**
     * 源ID
     */
    private byte[] srcId;
    /**
     * 目标ID
     */
    private byte[] destId;
    /**
     * 启用特性
     */
    private byte features;
    /**
     * 数据
     */
    private byte[] body;
    
    
    
    public void addFeature(byte feature){
        this.features |= feature;
    }

    public boolean hasFeature(byte feature){
        return (this.features & feature) != 0;
    }


    public byte getCs() {
        return cs;
    }

    public void setCs(byte cs) {
        this.cs = cs;
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

    public byte getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(byte serializeType) {
        this.serializeType = serializeType;
    }

    public byte[] getSrcId() {
        return srcId;
    }

    public void setSrcId(byte[] srcId) {
        this.srcId = srcId;
    }

    public byte[] getDestId() {
        return destId;
    }

    public void setDestId(byte[] destId) {
        this.destId = destId;
    }

    public byte getFeatures() {
        return features;
    }

    public void setFeatures(byte features) {
        this.features = features;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
