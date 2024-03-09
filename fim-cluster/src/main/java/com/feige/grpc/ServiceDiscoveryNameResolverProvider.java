package com.feige.grpc;

import com.feige.fim.srd.ServiceDiscovery;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;

import java.net.URI;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceDiscoveryNameResolverProvider extends NameResolverProvider {
    public static final String SCHEME = "discovery";
    private final ServiceDiscovery serviceDiscovery;
    private final Set<ServiceDiscoveryNameResolver> discoveryClientNameResolvers = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public ServiceDiscoveryNameResolverProvider(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 6;
    }

    @Override
    public NameResolver newNameResolver(URI uri, NameResolver.Args args) {
        if (SCHEME.equals(uri.getScheme())) {
            final String serviceName = uri.getPath();
            ServiceDiscoveryNameResolver nameResolver = new ServiceDiscoveryNameResolver(serviceName, serviceDiscovery, discoveryClientNameResolvers::remove, args);
            this.discoveryClientNameResolvers.add(nameResolver);
            return nameResolver;
        }
        return null;
    }

    @Override
    public String getDefaultScheme() {
        return SCHEME;
    }
}
