package com.feige.grpc.client;


import com.feige.api.sc.Listener;
import com.feige.api.sc.ServiceAdapter;
import com.feige.fim.protocol.Packet;
import com.feige.grpc.Message;
import com.feige.grpc.MessageServiceGrpc;
import com.feige.grpc.PacketAndMessageConverter;
import com.feige.grpc.utils.GrpcUtils;
import com.feige.api.rpc.RpcClient;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.channel.epoll.EpollDomainSocketChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.unix.DomainSocketAddress;

import java.util.concurrent.TimeUnit;

public class GrpcClient extends ServiceAdapter implements RpcClient<Packet> {
    
    protected ManagedChannel grpcChannel;
    protected MessageServiceGrpc.MessageServiceBlockingStub messageService;
    protected GrpcClientConf conf;

    public GrpcClient(GrpcClientConf conf) {
        this.conf = conf;
    }

    @Override
    public void initialize(){
        String address = conf.getAddress();
        NettyChannelBuilder nettyChannelBuilder;
        if (GrpcUtils.DOMAIN_SOCKET_ADDRESS_SCHEME.equals(conf.getScheme())) {
            final String path = GrpcUtils.extractDomainSocketAddressPath(address);
            nettyChannelBuilder = NettyChannelBuilder.forAddress(new DomainSocketAddress(path))
                    .channelType(EpollDomainSocketChannel.class)
                    .eventLoopGroup(new EpollEventLoopGroup());
        } else {
            nettyChannelBuilder = NettyChannelBuilder.forTarget(address)
                    .defaultLoadBalancingPolicy(conf.getLoadBalancingPolicy());
        }
        grpcChannel = configureChannel(nettyChannelBuilder);
        
    }

    protected ManagedChannel configureChannel(NettyChannelBuilder nettyChannelBuilder){
        return nettyChannelBuilder
                .usePlaintext()
                .build();
    }

    @Override
    protected void doStart(Listener listener) {
        messageService = MessageServiceGrpc.newBlockingStub(grpcChannel);
        super.doStart(listener);
    }

    @Override
    protected void doStop(Listener listener) {
        try {
            grpcChannel.shutdown().awaitTermination(5L, TimeUnit.SECONDS);
            super.doStop(listener);
        } catch (InterruptedException e) {
            listener.onFailure(e);
        }
    }

    @Override
    public Packet exchange(Packet packet) {
        Message messageReq = PacketAndMessageConverter.getInstance().convertT(packet);
        Message messageResp = messageService.sendMessage(messageReq);
        return PacketAndMessageConverter.getInstance().convertR(messageResp);
    }

    public ManagedChannel getGrpcChannel() {
        return grpcChannel;
    }
}
