package com.feige.fim.codec;


import com.feige.api.constant.Command;
import com.feige.api.constant.Const;

/**
 * version          byte类型      协议版本   1
 * bodyLength       int类型       消息体长度 4
 * srcId            long类型      源ID      8
 * destId           long类型      目标ID    8
 * cmd              byte类型      操作命令   1
 * serializeType    byte类型      序列化类型  1
 * features         byte类型      启用特性   1
 * cs               short类型      校验和    2
 * body             byte[]类型    数据
 */
public class Transport {


    public static Transport create(Command command){
        Transport transport = new Transport();
        transport.setCmd(command.getCmd());
        transport.setVersion(Const.VERSION);
        return transport;
    }

    private Transport(){

    }

    /**
     * 协议版本
     */
    private byte version;
    /**
     * 源ID
     */
    private long srcId;
    /**
     * 目标ID
     */
    private long destId;
    /**
     * 操作指令
     */
    private byte cmd;
    /**
     * 序列化类型
     */
    private byte serializeType;
    /**
     * 启用特性
     */
    private byte features;
    /**
     * 校验和
     */
    private short cs;
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


    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public long getSrcId() {
        return srcId;
    }

    public void setSrcId(long srcId) {
        this.srcId = srcId;
    }

    public long getDestId() {
        return destId;
    }

    public void setDestId(long destId) {
        this.destId = destId;
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

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public int getBodyLength(){
        return this.getBody().length;
    }
}
