package com.feige.api.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;


public interface Configs {

    String CONFIG_FILE_KEY = "fim.path";

    Config CONFIG = loadConfig();

    static Config loadConfig(){
        Config config = ConfigFactory.load();
        if (config.hasPath(CONFIG_FILE_KEY)){
            File file = new File(config.getString(CONFIG_FILE_KEY));
            if (file.exists()) {
                Config custom = ConfigFactory.parseFile(file);
                config = custom.withFallback(config);
            }
        }
        return config;
    }

    Config ROOT = (Config) Configs.CONFIG.getObject("fim");

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
