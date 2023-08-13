package com.feige.fim.config;


import com.feige.api.constant.ProtocolConst;
import com.feige.fim.utils.StringUtils;
import com.feige.framework.utils.Configs;
import org.bouncycastle.util.encoders.Base64;

import java.util.List;

public class ClientConfig {

    public static String serializeString() {
        String[] strings = {
                String.valueOf(getExpireTime()),
                getSessionId(),
                Base64.toBase64String(getClientKey()),
                Base64.toBase64String(getIv()),
                getToken()
        };
        return StringUtils.commaJoiner.join(strings);
    }

    public static boolean isExpired() {
        return getExpireTime() < System.currentTimeMillis();
    }

    public static void deserializeString(String str) {
        List<String> list = StringUtils.originCommaSplitter.splitToList(str);
        if (list.size() == 5){
            setExpireTime(Long.parseLong(list.get(0)));
            if (isExpired()){
                return;
            }
            setSessionId(list.get(1));
            setClientKey(Base64.decode(list.get(2)));
            setIv(Base64.decode(list.get(3)));
            setToken(list.get(4));
        }
    }

    public static String getSessionId() {
        return  Configs.getString(ClientConfigKey.SESSION_ID);
    }

    public static void setSessionId(String sessionId) {
        Configs.setConfig(ClientConfigKey.SESSION_ID, sessionId);
    }

    public static long getExpireTime() {
        return Configs.getLong(ClientConfigKey.EXPIRE_TIME, -1L);
    }

    public static void setExpireTime(long expireTime) {
        Configs.setConfig(ClientConfigKey.EXPIRE_TIME, expireTime);
    }

    public static byte[] getClientKey() {
        String clientKey = Configs.getString(ClientConfigKey.CLIENT_KEY);
        if (StringUtils.isNotBlank(clientKey)){
            return Base64.decode(clientKey);
        }
        return new byte[0];
    }

    public static void setClientKey(byte[] clientKey) {
        Configs.setConfig(ClientConfigKey.CLIENT_KEY, Base64.toBase64String(clientKey));
    }
    
    
    public static byte[] getIv() {
        String iv = Configs.getString(ClientConfigKey.IV);
        if (StringUtils.isNotBlank(iv)){
            return Base64.decode(iv);
        }
        return new byte[0];
    }

    public static void setIv(byte[] iv) {
        Configs.setConfig(ClientConfigKey.IV, Base64.toBase64String(iv));
    }

    public static String getClientVersion() {
        return Configs.getString(ClientConfigKey.CLIENT_VERSION);
    }

    public static void setClientVersion(String clientVersion) {
        Configs.setConfig(ClientConfigKey.CLIENT_VERSION, clientVersion);
    }

    public static String getClientId() {
        return Configs.getString(ClientConfigKey.CLIENT_ID);
    }

    public static void setClientId(String clientId) {
        Configs.setConfig(ClientConfigKey.CLIENT_ID, clientId);
    }

    public static String getOsName() {
        return Configs.getString(ClientConfigKey.OS_NAME);
    }

    public static void setOsName(String osName) {
        Configs.setConfig(ClientConfigKey.OS_NAME, osName);
    }

    public static String getOsVersion() {
        return Configs.getString(ClientConfigKey.OS_VERSION);
    }

    public static void setOsVersion(String osVersion) {
        Configs.setConfig(ClientConfigKey.OS_VERSION, osVersion);
    }

    public static int getClientType() {
        return Configs.getInt(ClientConfigKey.CLIENT_TYPE, 0);
    }

    public static void setClientType(int clientType) {
        Configs.setConfig(ClientConfigKey.CLIENT_TYPE, clientType);
    }

    public static String getToken() {
        return Configs.getString(ClientConfigKey.TOKEN);
    }

    public static void setToken(String token) {
        Configs.setConfig(ClientConfigKey.TOKEN, token);
    }

    public static String getServerIp() {
        return Configs.getString(ClientConfigKey.SERVER_IP);
    }

    public static void setServerIp(String serverIp) {
        Configs.setConfig(ClientConfigKey.SERVER_IP, serverIp);
    }

    public static int getServerPort() {
        return  Configs.getInt(ClientConfigKey.SERVER_PORT, 8000);
    }

    public static void setServerPort(int serverPort) {
        Configs.setConfig(ClientConfigKey.SERVER_PORT, serverPort);
    }

    public static byte[] getPublicKey(){
        String publicKey = Configs.getString(Configs.ConfigKey.CRYPTO_ASYMMETRIC_PUBLIC_KEY);
        if (StringUtils.isNotBlank(publicKey)){
            return Base64.decode(publicKey);
        }
        return new byte[0];
    }

    public static void setPublicKey(byte[] publicKey){
        Configs.setConfig(Configs.ConfigKey.CRYPTO_ASYMMETRIC_PUBLIC_KEY, Base64.toBase64String(publicKey));
    }
     
    public static boolean enableCrypto(){
        return Configs.getBoolean(Configs.ConfigKey.CRYPTO_ENABLE, false);
    }
    
    public static int getKeyLength(){
        return Configs.getInt(Configs.ConfigKey.CRYPTO_SYMMETRIC_KEY_LENGTH, 16);
    }
    
    public static void setKeyLength(int keyLength){
        Configs.setConfig(Configs.ConfigKey.CRYPTO_SYMMETRIC_KEY_LENGTH, keyLength);
    }
    
    public static void setEnableCrypto(boolean enableCrypto){
        Configs.setConfig(Configs.ConfigKey.CRYPTO_ENABLE, enableCrypto);
    }


    public static String getClientKeyString() {
        return Configs.getString(ClientConfigKey.CLIENT_KEY);
    }
    
    public static String getIvString(){
        return Configs.getString(ClientConfigKey.IV);
    }
    
    
    public static byte getSerializerType(){
        Integer value = Configs.getInt(ClientConfigKey.SERIALIZER_TYPE, (int) ProtocolConst.PROTOCOL_BUFFER);
        return value.byteValue();
    }
    
    public static void setSerializerType(byte serializerType){
        Configs.setConfig(ClientConfigKey.SERIALIZER_TYPE, (int) serializerType);
    }
    
    public static String getTags(){
        return Configs.getString(ClientConfigKey.TAGS);
    }


    public static void setTags(String tags) {
        Configs.setConfig(ClientConfigKey.TAGS, tags);
    }
    
}
