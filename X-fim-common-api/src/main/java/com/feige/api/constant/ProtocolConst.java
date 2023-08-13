package com.feige.api.constant;

public interface ProtocolConst {

    /**
     * protocol version
     */
    byte VERSION = 1;
    /**
     * ENCRYPT
     */
    byte ENCRYPT = 1;
    /**
     * compress
     */
    byte COMPRESS = 2;
    /**
     * business acknowledge
     */
    byte BIZ_ACK = 4;
    /**
     * automatic acknowledge
     */
    byte AUTO_ACK = 8;
    /**
     * serialization type
     */
    byte JSON = 1;
    byte PROTOCOL_BUFFER = 2;
    byte XML = 3;
    /**
     * heartbeat packet
     */
    byte HB_PACKET_BYTE = -33;
    
    int HEADER_LEN = 10;
    
    String CHECK_SUM_DEFAULT = "default";
    String CHECK_SUM_CRC32 = "crc32";
    
   enum SerializedClass {
       HANDSHAKE_REQ(1),
       FAST_CONNECT_REQ(2),
       HANDSHAKE_RESP(3),
       FAST_CONNECT_RESP(4),
       BIND_CLIENT(5),
       ACK(6),
       ERROR_RESP(7);
       SerializedClass(int classKey){
           this.classKey = (byte) classKey;
       }
       private final byte classKey;

       public byte getClassKey() {
           return classKey;
       }
   }
   
   enum ErrorCode {
       DUPLICATE_HANDSHAKE(100);
       
       private final int errorCode;

       public int getErrorCode() {
           return errorCode;
       }

       ErrorCode(int errorCode) {
           this.errorCode = errorCode;
       }
   }
}
