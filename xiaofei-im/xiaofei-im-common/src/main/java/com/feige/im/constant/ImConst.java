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
     * 消息长度所占字节数
     */
    int MSG_LENGTH = 4;

    /**
     * 消息类型所占字节数
     */
    int MSG_TYPE_LENGTH = 4;

    /**
     * 消息解析器类型所占字节数
     */
    int MSG_PARSER_TYPE_LENGTH = 4;

    /**
     * TCP消息头所占字节数，TCP消息：消息长度 + 解析器类型 + class类型 + 具体消息
     */
    int TCP_MSG_HEAD_LENGTH = 12;

    /**
     * WS消息头所占字节数，WS消息：解析器类型 + class类型 + 具体消息
     */
    int WS_MSG_HEAD_LENGTH = 8;

    /**
     * UDP消息头所占字节数，UDP消息：解析器类型 + class类型 + 具体消息
     */
    int UDP_MSG_HEAD_LENGTH = 8;

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


    String ALL = "all";
    String TCP = "tcp";
    String WS = "ws";
    String UDP = "udp";
    String HTTP = "http";


    /**
     * 配置信息
     */
    String SERVER_IP = "xiaofei.im.server.ip";
    String TCP_SERVER_PORT = "xiaofei.im.server.tcp.port";
    String WS_SERVER_PORT = "xiaofei.im.server.ws.port";
    String UDP_SERVER_PORT = "xiaofei.im.server.udp.port";
    /**
     * 开启哪些协议all,tcp,udp,ws,http，默认all
     */
    String OPEN_PROTOCOL = "xiaofei.im.open.protocol";

    // websocketPath
    String WS_PATH = "xiaofei.im.server.ws.path";
    String DEFAULT_WS_PATH = "/";

    String DEFAULT_LOG_NAME = "java";



}
