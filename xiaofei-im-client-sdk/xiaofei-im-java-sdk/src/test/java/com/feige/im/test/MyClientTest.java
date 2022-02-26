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
import com.feige.im.sender.WaitingAckTimerHandler;
import com.feige.im.utils.SnowflakeIdUtil;

import java.util.Scanner;

/**
 * @author feige<br />
 * @ClassName: MyClientTest <br/>
 * @Description: <br/>
 * @date: 2022/2/26 13:00<br/>
 */

public class MyClientTest {

    public static final Logger LOG = LoggerFactory.getLogger();
    public static final String IP = "localhost";
    public static final int PORT = 8001;
    public static final String SENDER_ID = "my";
    public static final String RECEIVER_ID = "receiver";


    public static void main(String[] args) {
        Parser.registerDefaultParsing();
        ClientStarter.start(IP, PORT, new DefaultClientMsgListener(), new MsgStatusListener() {
            @Override
            public void hasMsgSent(Long msgId) {
                LOG.info("有消息发送 msgId = {}", msgId);
            }

            @Override
            public void hasMsgArrived(Long msgId) {
                LOG.info("有消息送达 msgId = {}", msgId);
            }

            @Override
            public void timeoutMsg(Long msgId) {
                LOG.info("有超时消息 msgId = {}", msgId);
            }

            @Override
            public void exceptionMsg(Long msgId) {
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
                .setId(SnowflakeIdUtil.generateId())
                .setContent(content)
                .setMsgType(1)
                .setExtra("extra")
                .setFormat(1)
                .setSenderId(SENDER_ID)
                .setReceiverId(RECEIVER_ID)
                .setStatus(1)
                .setTimestamp(String.valueOf(System.currentTimeMillis()))
                .build();
        return DefaultMsg.TransportMsg.newBuilder()
                .setType(DefaultMsg.TransportMsg.MsgType.PRIVATE)
                .setMsg(msg)
                .build();
    }

    public static void sentAuth(){
        DefaultMsg.Auth auth = DefaultMsg.Auth.newBuilder()
                .setPlatform("android")
                .setToken(SENDER_ID)
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

