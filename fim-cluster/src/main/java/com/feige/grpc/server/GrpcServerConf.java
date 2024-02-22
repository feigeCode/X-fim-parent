package com.feige.grpc.server;

import io.grpc.BindableService;
import io.grpc.ServerInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GrpcServerConf {
    private String address;
    private int port;
    private Map<String, Object> extra;
    private List<BindableService> services = new ArrayList<>();
    private List<ServerInterceptor> interceptors = new ArrayList<>();

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    public List<BindableService> getServices() {
        return services;
    }

    public void setServices(List<BindableService> services) {
        this.services = services;
    }

    public List<ServerInterceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<ServerInterceptor> interceptors) {
        this.interceptors = interceptors;
    }
}
