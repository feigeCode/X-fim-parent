package com.feige.api.srd;

import java.util.Map;

/**
 * @author feige<br />
 * @ClassName: ServerInstance <br/>
 * @Description: <br/>
 * @date: 2021/11/11 11:24<br/>
 */
public interface IServerInstance {


    String getInstanceId();


    String getIp();



    int getPort();


    double getWeight();


    boolean isHealthy();


    boolean isEnabled();


    boolean isEphemeral();


    String getClusterName();


    String getServiceName();


    void setMetadata(Map<String, String> metadata);
    
    Map<String, String> getMetadata();

}
