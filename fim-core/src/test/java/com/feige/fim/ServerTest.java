package com.feige.fim;

import com.feige.api.crypto.Cipher;
import com.feige.api.crypto.CipherFactory;
import com.feige.fim.sc.Server;
import com.feige.fim.sc.ServerProvider;
import com.feige.framework.context.StandardApplicationContext;
import com.feige.framework.context.api.ApplicationContext;
import com.feige.framework.utils.Configs;
import com.feige.utils.crypto.RsaUtils;
import com.feige.utils.spi.annotation.SPI;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@SPI(interfaces = ApplicationContext.class)
public class ServerTest {
    public static final String CONFIG_PATH = "E:\\project\\my\\X-fim-parent\\conf\\fim.yaml";
    private static ApplicationContext applicationContext;
    
    @BeforeClass
    public static void initialize() throws Exception {
        System.out.println("initialize start...");
        System.setProperty(Configs.CONFIG_FILE_KEY, CONFIG_PATH);
        applicationContext = new StandardApplicationContext();
        System.out.println("initialize end...");
    }
    
    @Test
    public void createTcpServer(){
        final ServerProvider serverProvider = applicationContext.get("tcp", ServerProvider.class);
        Server server = serverProvider.get();
        server.syncStart();
        server.syncStop();
    }

    @Test
    public void createWsServer(){
        final ServerProvider serverProvider = applicationContext.get("ws", ServerProvider.class);
        Server server = serverProvider.get();
        server.syncStart();
        server.syncStop();
    }
    
    @Test
    public void aesTest() throws Exception {
        String key = "aesTestKey";
        String iv = "WrongIlengthmust";
        String data = "AES加解密";
        CipherFactory aesCipherFactory = applicationContext.get("aes", CipherFactory.class);
        Cipher cipher = aesCipherFactory.create(key.getBytes(), iv.getBytes());
        byte[] encrypt = cipher.encrypt(data.getBytes(StandardCharsets.UTF_8));
        byte[] decrypt = cipher.decrypt(encrypt);
        Assert.assertEquals(data, new String(decrypt, 0, decrypt.length));
    }
    
    @Test
    public void rsaTest(){
        String data = "RSA加解密";
        KeyPair keyPair = RsaUtils.generateKey();
        PrivateKey aPrivate = keyPair.getPrivate();
        PublicKey aPublic = keyPair.getPublic();
        CipherFactory rsaCipherFactory = applicationContext.get("rsa", CipherFactory.class);
        Cipher cipher = rsaCipherFactory.create(aPrivate.getEncoded(), aPublic.getEncoded());
        byte[] encrypt = cipher.encrypt(data.getBytes(StandardCharsets.UTF_8));
        byte[] decrypt = cipher.decrypt(encrypt);
        Assert.assertEquals(data, new String(decrypt, 0, decrypt.length));
    }
    
    @Test
    public void rsaKeyPair(){
        KeyPair keyPair = RsaUtils.generateKey();
        PrivateKey aPrivate = keyPair.getPrivate();
        PublicKey aPublic = keyPair.getPublic();
        System.out.println("private-key:" + Base64.toBase64String(aPrivate.getEncoded()));
        System.out.println("public-key:" + Base64.toBase64String(aPublic.getEncoded()));
    }

}
