package com.feige.api.srd;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;


/**
 * @author feige<br />
 * @ClassName: ServerInstance <br/>
 * @Description: <br/>
 * @date: 2021/11/11 11:24<br/>
 */
@Data
public class ServerInstance {

    private String instanceId;
    private String ip;
    private int port;
    private double weight = 1.0D;
    private boolean healthy = true;
    private boolean enabled = true;
    private boolean ephemeral = true;
    private String clusterName;
    private String serviceName;
    private Map<String, String> metadata = new HashMap<>();


    public ServerInstance() {
    }

    public ServerInstance(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    
}
