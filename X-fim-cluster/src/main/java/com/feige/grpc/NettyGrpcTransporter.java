package com.feige.grpc;

import com.feige.api.constant.ServerConfigKey;
import com.feige.api.session.SessionRepository;
import com.feige.api.srd.ServiceDiscovery;
import com.feige.fim.protocol.Packet;
import com.feige.api.rpc.RpcClient;
import com.feige.api.rpc.RpcServer;
import com.feige.api.rpc.RpcTransporter;
import com.feige.framework.annotation.InitMethod;
import com.feige.framework.annotation.Inject;
import com.feige.framework.aware.EnvironmentAware;
import com.feige.framework.env.api.Environment;
import com.feige.grpc.client.GrpcClient;
import com.feige.grpc.client.GrpcClientConf;
import com.feige.grpc.server.GrpcServer;
import com.feige.grpc.server.GrpcServerConf;
import com.feige.grpc.service.MessageService;
import com.feige.utils.spi.annotation.SpiComp;
import io.grpc.NameResolverRegistry;

import java.util.Map;

@SpiComp(value = "nettyGrpc", interfaces = RpcTransporter.class)
public class NettyGrpcTransporter implements RpcTransporter<Packet>, EnvironmentAware {
    
    private RpcClient<Packet> rpcClient;
    
    private RpcServer<Packet> rpcServer;
    
    @Inject
    private SessionRepository sessionRepository;
    
    @Inject
    private ServiceDiscovery serviceDiscovery;
    
    private Environment environment;
    
    private GrpcClientConf buildClientConf(){
        GrpcClientConf grpcClientConf = new GrpcClientConf();
        String address = environment.getString(ServerConfigKey.GRPC_CLIENT_ADDRESS);
        String scheme = environment.getString(ServerConfigKey.GRPC_CLIENT_SCHEME);
        String loadBalancePolicy = environment.getString(ServerConfigKey.GRPC_CLIENT_LOAD_BALANCING_POLICY);
        Map<String, Object> extra = environment.getMapByKeyPrefix(ServerConfigKey.GRPC_CLIENT_EXTRA_PREFIX);
        grpcClientConf.setAddress(address);
        grpcClientConf.setScheme(scheme);
        grpcClientConf.setLoadBalancingPolicy(loadBalancePolicy);
        grpcClientConf.setExtra(extra);
        grpcClientConf.getProviders().add(new ServiceDiscoveryNameResolverProvider(serviceDiscovery));
        return grpcClientConf;
    }
    
    
    private GrpcServerConf buildServerConf(){
        GrpcServerConf grpcServerConf = new GrpcServerConf();
        String address = environment.getString(ServerConfigKey.GRPC_SERVER_ADDRESS);
        Integer port = environment.getInt(ServerConfigKey.GRPC_SERVER_PORT);
        Map<String, Object> extra = environment.getMapByKeyPrefix(ServerConfigKey.GRPC_SERVER_EXTRA_PREFIX);
        grpcServerConf.setAddress(address);
        grpcServerConf.setPort(port);
        grpcServerConf.setExtra(extra);
        grpcServerConf.getServices().add(new MessageService(sessionRepository));
        return grpcServerConf;
    }

    @Override
    @InitMethod
    public void initialize() throws IllegalStateException {
        this.rpcClient = new GrpcClient(buildClientConf());
        this.rpcServer = new GrpcServer(buildServerConf());
        this.start();
    }

    @Override
    public void start(String... args) throws IllegalStateException {
        this.rpcClient.syncStart();
        this.rpcServer.syncStart();
    }

    @Override
    public void destroy() throws IllegalStateException {
        this.rpcClient.syncStop();
        this.rpcServer.syncStop();
    }


    @Override
    public RpcServer<Packet> rpcServer() {
        return rpcServer;
    }

    @Override
    public RpcClient<Packet> rpcClient() {
        return rpcClient;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
