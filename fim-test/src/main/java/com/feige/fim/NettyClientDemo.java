package com.feige.fim;

import com.feige.api.constant.ClientType;
import com.feige.api.constant.Command;
import com.feige.api.constant.ProtocolConst;
import com.feige.api.msg.ChatMsgReq;
import com.feige.api.sc.Client;
import com.feige.api.sc.ClientProvider;
import com.feige.api.session.Session;
import com.feige.fim.config.ClientConfig;
import com.feige.fim.protocol.Packet;
import com.feige.fim.utils.PacketUtils;
import com.feige.framework.context.StandardApplicationContext;
import com.feige.framework.context.api.ApplicationContext;
import com.feige.framework.utils.Configs;
import com.feige.utils.crypto.CryptoUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class NettyClientDemo {

    public static final String CONFIG_PATH = "E:\\project\\my\\X-fim-parent\\conf\\fim-client.yaml";

    
    public static void main(String[] args) throws Exception {
        createClient(args, "1");
    }
    public static void createClient(String[] args, String clientId){
        System.setProperty(Configs.CONFIG_FILE_KEY, CONFIG_PATH);
        ApplicationContext applicationContext = new StandardApplicationContext();
        applicationContext.start(args);
        ClientConfig.setClientKey(CryptoUtils.randomAesKey(16));
        ClientConfig.setIv(CryptoUtils.randomAesIv(16));
        ClientConfig.setClientId(clientId);
        ClientConfig.setClientVersion("1.0");
        ClientConfig.setClientType(ClientType.ANDROID.getCode());
        ClientConfig.setOsName("android");
        ClientConfig.setOsVersion("14.0");
        ClientConfig.setToken("123");
        ClientConfig.setServerIp("127.0.0.1");
        ClientConfig.setServerPort(8001);
        ClientConfig.setTags("test");
        ClientProvider clientProvider = applicationContext.get(ClientProvider.class);
        Client client = clientProvider.get();
        client.syncStart();
        while (true){
            System.out.println("请输入消息内容：");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String msg = br.readLine();
                Packet packet = PacketUtils.buildPacket(Command.SINGLE_CHAT, ProtocolConst.SerializedClass.CHAT_MSG_REQ, (ChatMsgReq req) -> {
                    req.setContent(msg)
                            .setStatus(1)
                            .setFormat(1)
                            .setMsgType(1)
                            .setSenderId(clientId)
                            .setReceiverId("2".equals(clientId) ? "1" : "2")
                            .setSendTime(System.currentTimeMillis())
                            .setExtra("1")
                            .setId("1");
                });
                packet.addFeature(ProtocolConst.AUTO_ACK);
                Session session = client.getSession();
                session.write(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
