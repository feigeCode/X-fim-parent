package com.feige.im.config;

import com.feige.im.constant.ImConst;

import java.util.Properties;

/**
 * @author feige<br />
 * @ClassName: ClusterConfig <br/>
 * @Description: <br/>
 * @date: 2021/11/6 17:44<br/>
 */
public class ClusterConfig extends Properties {

    private ClusterConfig(){

    }

    private static volatile ClusterConfig clusterConfig;


    public static ClusterConfig getClusterConfig() {
        if (clusterConfig == null){
            synchronized (ClusterConfig.class){
                if (clusterConfig == null){
                    clusterConfig = new ClusterConfig();
                }
            }
        }
        return clusterConfig;
    }

    public String getConfigByKey(String key){
        if (key.equals(ImConst.NODE_KEY)){
            return "localhost:8100";
        }
        return this.getProperty(key);
    }
}
