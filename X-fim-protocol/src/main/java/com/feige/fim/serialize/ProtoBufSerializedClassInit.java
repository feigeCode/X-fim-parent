package com.feige.fim.serialize;

import com.feige.api.constant.ProtocolConst;
import com.feige.api.msg.FastConnectReq;
import com.feige.api.msg.HandshakeReq;
import com.feige.api.msg.HandshakeResp;
import com.feige.api.msg.Msg;
import com.feige.api.serialize.SerializedClassManager;
import com.feige.fim.msg.proto.FastConnectReqProto;
import com.feige.fim.msg.proto.HandshakeReqProto;
import com.feige.fim.msg.proto.HandshakeRespProto;
public class ProtoBufSerializedClassInit {
    
    
    private final SerializedClassManager serializedClassManager;

    public ProtoBufSerializedClassInit(SerializedClassManager serializedClassManager) {
        this.serializedClassManager = serializedClassManager;
    }

    public void initialize() {
        generateClass();
    }
    
    private void generateClass(){
        generateClass(HandshakeReq.TYPE, HandshakeReqProto.class, HandshakeReqProto.Builder.class);
        generateClass(HandshakeResp.TYPE, HandshakeRespProto.class, HandshakeRespProto.Builder.class);
        generateClass(FastConnectReq.TYPE, FastConnectReqProto.class, FastConnectReqProto.Builder.class);
    }
    
    private <T extends Msg> void generateClass(Class<T> type, Object... args){
        serializedClassManager.getClass(ProtocolConst.PROTOCOL_BUFFER, type, args == null || args.length == 0 ? null : () -> args);
    }
}
