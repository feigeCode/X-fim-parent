package com.feige.grpc.client;

import io.grpc.NameResolverProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GrpcClientConf {


    private String address;
    private String scheme;
    private String loadBalancingPolicy;
    private Map<String, Object> extra;
    private List<NameResolverProvider> providers = new ArrayList<>();

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getLoadBalancingPolicy() {
        return loadBalancingPolicy;
    }

    public void setLoadBalancingPolicy(String loadBalancingPolicy) {
        this.loadBalancingPolicy = loadBalancingPolicy;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    public List<NameResolverProvider> getProviders() {
        return providers;
    }

    public void setProviders(List<NameResolverProvider> providers) {
        this.providers = providers;
    }
}
