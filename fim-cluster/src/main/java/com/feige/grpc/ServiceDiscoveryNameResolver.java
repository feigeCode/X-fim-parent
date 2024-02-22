package com.feige.grpc;

import com.feige.api.sc.Callback;
import com.feige.api.srd.ServerInstance;
import com.feige.api.srd.ServiceDiscovery;
import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class ServiceDiscoveryNameResolver extends NameResolver {
    
    private final ServiceDiscovery serviceDiscovery;
    private final String serviceName;
    private final AtomicReference<List<ServerInstance>> instanceListRef = new AtomicReference<>(Collections.emptyList());
    private  Listener2 listener2;
    private final Callback<ServiceDiscoveryNameResolver> shutdownCallback;
    private final Args args;
    private volatile boolean running;
    private volatile boolean started;


    public ServiceDiscoveryNameResolver(String serviceName, ServiceDiscovery serviceDiscovery, Callback<ServiceDiscoveryNameResolver> shutdownCallback, Args args) {
        this.serviceName = serviceName;
        this.serviceDiscovery = serviceDiscovery;
        this.shutdownCallback = shutdownCallback;
        this.args = args;
    }

    @Override
    public String getServiceAuthority() {
        return this.serviceName;
    }

    @Override
    public void start(Listener2 listener) {
        if (this.started){
            throw new IllegalStateException("ServiceDiscoveryNameResolver started");
        }
        this.listener2 = listener;
        this.serviceDiscovery.subscribe(this.serviceName, this::resolve);
        this.args.getScheduledExecutorService().scheduleAtFixedRate(() -> this.resolve(null), 0, 60, TimeUnit.SECONDS);
        this.started = true;
    }
    
    @Override
    public void shutdown() {
        this.listener2 = null;
        this.instanceListRef.set(Collections.emptyList());
        this.serviceDiscovery.unsubscribe(this.serviceName, serverInstances -> {});
        if (this.shutdownCallback != null) {
            this.shutdownCallback.call(this);
        }
    }

    @Override
    public void refresh() {
        args.getScheduledExecutorService().execute(() -> resolve(null));
    }

    private void resolve(List<ServerInstance> allServerInstances){
        if (this.running){
            return;
        }
        this.running = true;
        try {
            List<ServerInstance> tempServerInstances = allServerInstances;
            if (CollectionUtils.isEmpty(tempServerInstances)){
                tempServerInstances = serviceDiscovery.getAllServerInstances(this.serviceName);
            }
            if (isNeedUpdate(tempServerInstances)){
                this.listener2.onResult(ResolutionResult.newBuilder()
                        .setAddresses(toAddressGroups(tempServerInstances))
                        .build());
                this.instanceListRef.set(tempServerInstances);
                log.info("update instance list [{}]", this.instanceListRef.get());
            }
        }catch (Throwable cause){
            this.listener2.onError(Status.UNAVAILABLE.withCause(cause)
                    .withDescription("update fail [" + this.serviceName + "]"));
            this.instanceListRef.set(Collections.emptyList());
        }finally {
            this.running = false;
        }
    }
    
    
    private boolean isNeedUpdate(List<ServerInstance> tempServerInstances){
        List<ServerInstance> serverInstances = this.instanceListRef.get();
        return !CollectionUtils.isEqualCollection(tempServerInstances, serverInstances);
    }

    private List<EquivalentAddressGroup> toAddressGroups(List<ServerInstance> newInstanceList) {
        List<EquivalentAddressGroup> targets = new ArrayList<>();
        for (ServerInstance instance : newInstanceList) {
            targets.add(toAddressGroup(instance));
        }
        return targets;
    }

    private EquivalentAddressGroup toAddressGroup(ServerInstance instance) {
        String host = instance.getIp();
        int port = instance.getPort();
        Attributes attributes = buildAttributes(instance);
        return new EquivalentAddressGroup(new InetSocketAddress(host, port), attributes);
    }

    protected Attributes buildAttributes(ServerInstance instance) {
        Attributes.Builder builder = Attributes.newBuilder();
        builder.set(Attributes.Key.create("serviceName"), this.serviceName);
        builder.set(Attributes.Key.create("instanceId"), instance.getInstanceId());
        builder.set(Attributes.Key.create("clusterName"), instance.getClusterName());
        builder.set(Attributes.Key.create("weight"), instance.getWeight());
        builder.set(Attributes.Key.create("metadata"), instance.getMetadata());
        builder.set(Attributes.Key.create("healthy"), instance.isHealthy());
        builder.set(Attributes.Key.create("enabled"), instance.isEnabled());
        builder.set(Attributes.Key.create("ephemeral"), instance.isEphemeral());
        return builder.build();
    }
}
