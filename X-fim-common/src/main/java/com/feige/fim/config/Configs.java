package com.feige.fim.config;

import com.feig.utils.ConfigUtil;
import com.typesafe.config.Config;


public interface Configs {

    Config ROOT = (Config) ConfigUtil.CONFIG.getObject("fim");
    /**
     * 日志相关配置
     */
    interface Log {
        Config LOG = (Config) ROOT.getObject("log");
        /**
         * 日志输出目录
         */
        String DIR = LOG.getString("dir");
        boolean USE_PID = LOG.getBoolean("use-pid");
        String OUTPUT_TYPE = LOG.getString("output-type");
        String CHARSET = LOG.getString("charset");
        /**
         * 配置文件路径
         */
        String CONFIG_PATH = LOG.getString("conf-path");
    }


    interface Server {
        Config SERVER = (Config) ROOT.getObject("server");
        boolean ENABLE_EPOLL = SERVER.getBoolean("enable-epoll");
        boolean ENABLE_TCP = Server.SERVER.getBoolean("enable-tcp");
        int TCP_PORT = SERVER.getInt("tcp-port");
        boolean ENABLE_WS = Server.SERVER.getBoolean("enable-ws");
        boolean ENABLE_HTTP = Server.SERVER.getBoolean("enable-http");
        int WS_PORT = SERVER.getInt("ws-port");
        boolean ENABLE_UDP = Server.SERVER.getBoolean("enable-udp");
        int UDP_PORT = SERVER.getInt("udp-port");
    }

}
