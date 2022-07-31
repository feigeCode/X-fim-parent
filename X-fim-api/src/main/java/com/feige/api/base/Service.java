package com.feige.api.base;


import java.util.Map;

public interface Service {

    /**
     * 初始化
     * @param args 参数
     */
    void init(Map<String, Object> args);

    /**
     * 销毁
     * @param args 参数
     */
    void destroy(Map<String, Object> args);
}
