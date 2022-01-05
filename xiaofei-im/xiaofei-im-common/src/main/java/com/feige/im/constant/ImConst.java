package com.feige.im.constant;

import com.feige.im.pojo.proto.DefaultMsg;

/**
 * @author feige<br />
 * @ClassName: ImConst <br/>
 * @Description: <br/>
 * @date: 2021/10/6 22:05<br/>
 */
public interface ImConst {

    /**
     * 心跳超时次数
     */
    int PONG_TIME_OUT_COUNT = 3;

    /**
     * 心跳类型消息
     */
    byte PING_MSG_TYPE = 1;

    byte PONG_MSG_TYPE = 0;

    DefaultMsg.Ping PING_MSG = DefaultMsg.Ping.newBuilder().setPing(1).build();

    DefaultMsg.Pong PONG_MSG = DefaultMsg.Pong.newBuilder().setPong(0).build();


    String ANDROID = "android";
    String IOS = "ios";
    String MAC = "mac";
    String WINDOWS = "windows";
    String WEB = "web";


    /**
     * 配置信息
     */
    String SERVER_IP = "server.ip";
    String SERVER_PORT = "server.port";


    String DEFAULT_LOG_NAME = "java";



}
