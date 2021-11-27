package com.feige.im;

import com.feige.im.client.ImClient;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
import io.netty.channel.Channel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author feige<br />
 * @ClassName: Main <br/>
 * @Description: <br/>
 * @date: 2021/11/6 23:19<br/>
 */
public class ClientMain2 {

    public static void main(String[] args) throws IOException {
        Parser.registerDefaultParsing();
        ImClient localhost = ImClient.connect("192.168.0.100", 8002, (key, channel, msg,throwable) -> {
            System.out.println(msg);
        });
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            String s = bufferedReader.readLine();
            Channel channel = localhost.getChannel();
            if ("1".equals(s)){
                DefaultMsg.Auth auth = DefaultMsg.Auth.newBuilder()
                        .setPlatform("android")
                        .setUserId("34353")
                        .setDeviceName("w")
                        .setDeviceId("1")
                        .setLanguage("en")
                        .setOsVersion("1.0")
                        .setToken("token")
                        .build();
                channel.writeAndFlush(auth);
            }else {
                DefaultMsg.Msg msg = DefaultMsg.Msg.newBuilder()
                        .setId(123456L)
                        .setContent("hello netty!")
                        .setContentType(1)
                        .setMsgType(1)
                        .setExtra("extra")
                        .setFormat(1)
                        .setSenderId("34353")
                        .setReceiverId("1")
                        .setTimestamp(String.valueOf(System.currentTimeMillis()))
                        .build();
                channel.writeAndFlush(msg);
            }

        }
    }
}
