package com.feige.api.constant;

import java.util.HashMap;
import java.util.Map;

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
    
    int HEADER_LEN = 11;
    
    String CHECK_SUM_DEFAULT = "default";
    String CHECK_SUM_CRC32 = "crc32";
    
   enum SerializedClass {
       HANDSHAKE_REQ(1),
       FAST_CONNECT_REQ(2),
       HANDSHAKE_RESP(3),
       SUCCESS_RESP(4),
       BIND_CLIENT(5),
       ACK(6),
       ERROR_RESP(7, true);
       SerializedClass(int classKey){
           this(classKey, false);
       }
       SerializedClass(int classKey, boolean customCrypto){
           this.classKey = (byte) classKey;
           this.customCrypto = customCrypto;
       }
       private final byte classKey;
       private final boolean customCrypto;
       
       public byte getClassKey() {
           return classKey;
       }
       
       public boolean isCustomCrypto(){
           return this.customCrypto;
       }
       private static final Map<Byte, SerializedClass> allSerializedClassesMap = new HashMap<Byte, SerializedClass>(){{
           SerializedClass[] values = SerializedClass.values();
           for (SerializedClass serializedClass : values) {
               put(serializedClass.getClassKey(), serializedClass);
           }
       }};
       public static boolean isCustomCrypto(byte classKey){
           SerializedClass serializedClass = allSerializedClassesMap.get(classKey);
           if (serializedClass == null){
               throw new IllegalArgumentException("class key [" + classKey + "] not found");
           }
           return serializedClass.isCustomCrypto();
       }
   }
   
   enum ErrorCode {
       DUPLICATE_HANDSHAKE(5000),

       ILLEGAL_TOKEN(5001),
       
       ILLEGAL_KEY_LENGTH(5002),

       ILLEGAL_SESSION(5003),

       NOT_HANDSHAKE(5004),
       
       DUPLICATE_BIND(5005),
       
       NOT_BIND(5006),
               
       ;
       
       private final int errorCode;

       public int getErrorCode() {
           return errorCode;
       }

       ErrorCode(int errorCode) {
           this.errorCode = errorCode;
       }
   }

    enum SuccessCode {
        FAST_CONNECT_SUCCESS(2000),

        BIND_SUCCESS(2001),
        ;

        private final int statusCode;

        public int getStatusCode() {
            return statusCode;
        }

        SuccessCode(int statusCode) {
            this.statusCode = statusCode;
        }
    }
   
   
   
}
