package com.feige.grpc.server;

import com.feige.api.sc.Listener;
import com.feige.api.sc.ServiceAdapter;
import com.feige.fim.protocol.Packet;
import com.feige.grpc.utils.GrpcUtils;
import com.feige.fim.rpc.RpcServer;
import com.google.common.net.InetAddresses;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerInterceptor;
import io.grpc.netty.NettyServerBuilder;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerDomainSocketChannel;
import io.netty.channel.unix.DomainSocketAddress;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GrpcServer extends ServiceAdapter implements RpcServer<Packet> {
    protected GrpcServerConf conf;
    protected Server server;

    public GrpcServer(GrpcServerConf conf) {
        this.conf = conf;
    }

    @Override
    public void initialize() throws IllegalStateException {
        final String address = conf.getAddress();
        final int port = conf.getPort();
        NettyServerBuilder nettyServerBuilder;
        if (address.startsWith(GrpcUtils.DOMAIN_SOCKET_ADDRESS_SCHEME + ":")) {
            final String path = GrpcUtils.extractDomainSocketAddressPath(address);
            nettyServerBuilder = NettyServerBuilder.forAddress(new DomainSocketAddress(path))
                    .channelType(EpollServerDomainSocketChannel.class)
                    .bossEventLoopGroup(new EpollEventLoopGroup(1))
                    .workerEventLoopGroup(new EpollEventLoopGroup());
        } else if (GrpcUtils.ANY_IP_ADDRESS.equals(address)) {
            nettyServerBuilder = NettyServerBuilder.forPort(port);
        } else {
            nettyServerBuilder = NettyServerBuilder.forAddress(new InetSocketAddress(InetAddresses.forString(address), port));
        }
        this.server = configureServer(nettyServerBuilder);
    }
    
    
    protected Server configureServer(NettyServerBuilder nettyServerBuilder){
        List<BindableService> services = conf.getServices();
        for (BindableService service : services) {
            nettyServerBuilder.addService(service);
        }
        List<ServerInterceptor> interceptors = conf.getInterceptors();
        for (ServerInterceptor interceptor : interceptors) {
            nettyServerBuilder.intercept(interceptor);
        }
        return nettyServerBuilder
                .build();
    }


    @Override
    protected void doStart(Listener listener) {
        try {
            this.server.start();
            super.doStart(listener);
        } catch (IOException e) {
            listener.onFailure(e);
        }
    }

    @Override
    protected void doStop(Listener listener) {
        try {
            this.server.shutdown().awaitTermination(5L, TimeUnit.SECONDS);
            super.doStop(listener);
        } catch (InterruptedException e) {
            listener.onFailure(e);
        }
    }

    public Server getServer() {
        return server;
    }
}
