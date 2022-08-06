package com.feige.api.base;


import java.util.Map;

public interface Service {

    /**
     * 初始化
     * @param args 参数
     */
    void init(Map<String, Object> args);

    /**
     * 启动
     */
    void start();

    /**
     * 停止
     */
    void stop();

    /**
     * 运行状态
     * @return
     */
    boolean isRunning();

}
