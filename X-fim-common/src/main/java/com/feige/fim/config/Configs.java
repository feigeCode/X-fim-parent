package com.feige.fim.config;

import com.feig.utils.ConfigUtil;
import com.typesafe.config.Config;


public interface Configs {

    Config ROOT = (Config) ConfigUtil.CONFIG.getObject("fim");
    /**
     * 日志相关配置
     */
    interface Log {
        String DIR = ROOT.getString("dir");
        boolean USE_PID = ROOT.getBoolean("use-pid");
        String OUTPUT_TYPE = ROOT.getString("output-type");
        String CHARSET = ROOT.getString("charset");
        String CONFIG_PATH = ROOT.getString("conf-path");
    }

}
