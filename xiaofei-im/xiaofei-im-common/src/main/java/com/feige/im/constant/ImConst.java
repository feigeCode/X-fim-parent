package com.feige.im.constant;

import com.feige.im.pojo.proto.HeartBeat;

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
    int PING_MSG_TYPE = 1;

    int PONG_MSG_TYPE = 0;

    HeartBeat.Ping PING_MSG = HeartBeat.Ping.newBuilder().setPing(1).build();

    HeartBeat.Pong PONG_MSG = HeartBeat.Pong.newBuilder().setPong(0).build();


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
