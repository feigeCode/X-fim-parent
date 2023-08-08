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
       HANDSHAKE(1),
       FAST_CONNECT(2),
       ACK(3);
       SerializedClass(int classKey){
           this.classKey = (byte) classKey;
       }
       private final byte classKey;

       public byte getClassKey() {
           return classKey;
       }
   }
}
