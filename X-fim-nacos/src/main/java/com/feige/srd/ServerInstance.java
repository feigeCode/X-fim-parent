package com.feige.srd;

import com.feige.api.srd.IServerInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * @author feige<br />
 * @ClassName: ServerInstance <br/>
 * @Description: <br/>
 * @date: 2021/11/11 11:24<br/>
 */
public class ServerInstance implements IServerInstance {

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

    @Override
    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEphemeral() {
        return ephemeral;
    }

    public void setEphemeral(boolean ephemeral) {
        this.ephemeral = ephemeral;
    }

    @Override
    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public Map<String, String> getMetadata() {
        return metadata;
    }

    @Override
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "ServerInstance{" +
                "instanceId='" + instanceId + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", weight=" + weight +
                ", healthy=" + healthy +
                ", enabled=" + enabled +
                ", ephemeral=" + ephemeral +
                ", clusterName='" + clusterName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
