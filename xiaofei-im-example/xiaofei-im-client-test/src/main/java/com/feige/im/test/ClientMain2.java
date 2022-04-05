package com.feige.im.test;

import com.feige.im.client.ImClient;
import com.feige.im.handler.MsgListener;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
import com.google.protobuf.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

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
        ImClient localhost = ImClient.connect("10.1.204.70", 8002, new MsgListener() {
            @Override
            public void active(ChannelHandlerContext ctx) {

            }

            @Override
            public void read(ChannelHandlerContext ctx, Message msg) {

            }

            @Override
            public void inactive(ChannelHandlerContext ctx) {

            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

            }
        });
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            String s = bufferedReader.readLine();
            Channel channel = localhost.getChannel();
            if ("1".equals(s)){
                DefaultMsg.Auth auth = DefaultMsg.Auth.newBuilder()
                        .setPlatform("android")
                        .setToken("34353")
                        .setDeviceName("w")
                        .setDeviceId("1")
                        .setLanguage("en")
                        .setOsVersion("1.0")
                        .setVersion("1.0")
                        .setAddress("四川省成都市双流区")
                        .setIp("10.1.204.70")
                        .build();
                channel.writeAndFlush(auth);
            }else {
                DefaultMsg.Msg msg = DefaultMsg.Msg.newBuilder()
                        .setId("123456")
                        .setContent("hello netty!")
                        .setMsgType(1)
                        .setExtra("extra")
                        .setFormat(1)
                        .setSenderId("34353")
                        .setReceiverId("0")
                        .setStatus(1)
                        .setGmtCreate(String.valueOf(System.currentTimeMillis()))
                        .build();
                DefaultMsg.TransportMsg transportMsg = DefaultMsg.TransportMsg.newBuilder()
                        .setType(DefaultMsg.TransportMsg.MsgType.PRIVATE)
                        .setMsg(msg)
                        .build();
                channel.writeAndFlush(transportMsg);
            }

        }
    }
}
