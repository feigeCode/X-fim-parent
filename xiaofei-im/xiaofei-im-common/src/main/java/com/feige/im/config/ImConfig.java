package com.feige.im.config;


import com.feige.im.constant.ImConst;
import com.feige.im.utils.AssertUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author feige<br />
 * @ClassName: ClusterConfig <br/>
 * @Description: <br/>
 * @date: 2021/11/6 17:44<br/>
 */
public class ImConfig extends Properties {

    private static final Logger LOG = LogManager.getLogger(ImConfig.class);

    private ImConfig(){
    }

    private static volatile ImConfig imConfig;


    public static ImConfig getInstance() {
        if (imConfig == null){
            synchronized (ImConfig.class){
                if (imConfig == null){
                    imConfig = new ImConfig();
                }
            }
        }
        return imConfig;
    }

    /**
     * @description: 加载配置文件
     * @author: feige
     * @date: 2021/11/14 16:03
     * @param	file	文件对象
     * @return: void
     */
    public void loadProperties(File file){
        if (!file.exists()){
            LOG.error("路径{}不存在，请检查配置！",file.getAbsolutePath());
            return;
        }
        if (!file.isFile()){
            LOG.error("路径{}不是文件，请检查配置！",file.getAbsolutePath());
            return;
        }
        try {
            LOG.info("配置文件路径为：{}", file.getAbsolutePath());
            this.loadProperties(new FileInputStream(file));
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    /**
     * @description: 加载配置文件
     * @author: feige
     * @date: 2021/11/14 16:03
     * @param	is
     * @return: void
     */
    public void loadProperties(InputStream is){
        try {
            LOG.info("准备加载配置文件");
            this.load(is);
            LOG.info("加载配置文件完成");
        } catch (IOException e) {
            LOG.error(e);
        }
    }

   /**
    * @description: 通过key获取配置
    * @author: feige
    * @date: 2021/11/14 16:02
    * @param	key	配置key
    * @return: java.lang.String
    */
    public String getConfigByKey(String key){
        return this.getProperty(key);
    }


    /**
     * @description:
     * @author: feige
     * @date: 2021/11/14 16:02
     * @param	key	配置key
     * @param	value 配置value
     * @return: void
     */
    public void setConfigByKey(String key,String value){
        this.put(key,value);
    }

    /**
     * @description: 获取节点key，ip:port
     * @author: feige
     * @date: 2021/11/14 17:09
     * @param
     * @return: java.lang.String
     */
    public String getNodeKey(){
        String serverIp = getProperty(ImConst.SERVER_IP);
        String serverPort = getProperty(ImConst.SERVER_PORT);
        AssertUtil.notBlank(serverIp,"server.ip");
        AssertUtil.notBlank(serverPort,"server.port");
        return serverIp + ":" + serverPort;
    }

}
