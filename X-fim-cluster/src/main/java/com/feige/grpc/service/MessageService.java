package com.feige.grpc.service;

import com.feige.api.handler.RemotingException;
import com.feige.api.session.SessionRepository;
import com.feige.fim.protocol.Packet;
import com.feige.grpc.Message;
import com.feige.grpc.MessageServiceGrpc;
import com.feige.grpc.PacketAndMessageConverter;
import com.google.protobuf.Any;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

@Slf4j
public class MessageService extends MessageServiceGrpc.MessageServiceImplBase {
    
    private final SessionRepository sessionRepository;

    public MessageService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void sendMessage(Message request, StreamObserver<Message> responseObserver) {
        Packet packet = PacketAndMessageConverter.getInstance().convertR(request);
        String receiverId = getReceiverId(request);
        try {
            sessionRepository.write(receiverId, packet);
        } catch (RemotingException e) {
            log.error("push message error:", e);
        }
    }
    
    
    private String getReceiverId(Message request){
        Map<String, Any> extraMap = request.getExtraMap();
        return MapUtils.getString(extraMap, "receiverId");
    }
}
