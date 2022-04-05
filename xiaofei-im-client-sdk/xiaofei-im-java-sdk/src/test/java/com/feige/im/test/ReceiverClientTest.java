package com.feige.im.test;


import com.feige.im.ClientStarter;
import com.feige.im.listener.DefaultClientMsgListener;
import com.feige.im.listener.MsgStatusListener;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.receiver.AckFactory;
import com.feige.im.sender.DefaultPushManager;
import com.feige.im.sender.PushManager;
import com.feige.im.utils.SnowflakeIdUtil;

import java.util.Scanner;

/**
 * @author feige<br />
 * @ClassName: MyClientTest <br/>
 * @Description: <br/>
 * @date: 2022/2/26 13:00<br/>
 */

public class ReceiverClientTest {

    public static final Logger LOG = LoggerFactory.getLogger();
    public static final String IP = "10.1.121.155";
    public static final int PORT = 8001;
    public static final String SENDER_ID = "1391025558363648002";
    public static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJSMjS2NDQwMjU1tTA2MzYzsTAwMFLSUcpMLFGyMjQzMTcxMTI1N9dRSq0oAAtYGJiYG4MEMlNwas5LzE0FSj6b2f20YyWIX5oL5BoZGFoYmhgbGpoChVJzEzNzQIKm5pYW5pZA3Q6FhXrJ-blKtQDn2l4-mQAAAA.bJ3_BiL6yiNnwLqXGKuysQ4ADJsFlvOjhDiXd7c6Acw";
    public static final String RECEIVER_ID = "1390961159053549569";


    public static void main(String[] args) {
        Parser.registerDefaultParsing();
        ClientStarter.start(IP, PORT, new DefaultClientMsgListener(), new MsgStatusListener() {
            @Override
            public void hasMsgSent(String msgId) {
                LOG.info("有消息发送 msgId = {}", msgId);
            }

            @Override
            public void hasMsgArrived(String msgId) {
                LOG.info("有消息送达 msgId = {}", msgId);
            }

            @Override
            public void timeoutMsg(String msgId) {
                LOG.info("有超时消息 msgId = {}", msgId);
            }

            @Override
            public void exceptionMsg(String msgId) {
                LOG.info("有异常消息 msgId = {}", msgId);
            }
        });
        AckFactory.setSenderId(SENDER_ID);
        sentAuth();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true){
                System.out.println("请输入发送的内容：");
                String msg = scanner.nextLine();
                if ("exit".equals(msg.trim())){
                    break;
                }
                DefaultPushManager.pushMsg(getMsg(msg));
            }

        }

    }


    public static DefaultMsg.TransportMsg getMsg(String content){
        DefaultMsg.Msg msg = DefaultMsg.Msg.newBuilder()
                .setId(String.valueOf(SnowflakeIdUtil.generateId()))
                .setContent(content)
                .setMsgType(1)
                .setExtra("extra")
                .setFormat(1)
                .setSenderId(SENDER_ID)
                .setReceiverId(RECEIVER_ID)
                .setStatus(1)
                .setGmtCreate(String.valueOf(System.currentTimeMillis()))
                .build();
        return DefaultMsg.TransportMsg.newBuilder()
                .setType(DefaultMsg.TransportMsg.MsgType.PRIVATE)
                .setMsg(msg)
                .build();
    }

    public static void sentAuth(){
        DefaultMsg.Auth auth = DefaultMsg.Auth.newBuilder()
                .setPlatform("android")
                .setToken(TOKEN)
                .setDeviceName("w")
                .setDeviceId("1")
                .setLanguage("en")
                .setOsVersion("1.0").setVersion("1.0")
                .setAddress("四川省成都市双流区")
                .setIp("10.1.204.70")
                .build();
        PushManager.push(auth);
    }


}

