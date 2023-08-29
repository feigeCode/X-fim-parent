package com.feige.fim.serialize;

import com.feige.api.constant.ProtocolConst;
import com.feige.api.msg.BindClientReq;
import com.feige.api.msg.ErrorResp;
import com.feige.api.msg.FastConnectReq;
import com.feige.api.msg.FastConnectResp;
import com.feige.api.msg.HandshakeReq;
import com.feige.api.msg.HandshakeResp;
import com.feige.api.msg.Msg;
import com.feige.api.serialize.SerializedClassInit;
import com.feige.api.serialize.SerializedClassManager;
import com.feige.fim.msg.proto.BindClientReqProto;
import com.feige.fim.msg.proto.ErrorRespProto;
import com.feige.fim.msg.proto.FastConnectReqProto;
import com.feige.fim.msg.proto.FastConnectRespProto;
import com.feige.fim.msg.proto.HandshakeReqProto;
import com.feige.fim.msg.proto.HandshakeRespProto;
import com.feige.framework.annotation.Inject;
import com.feige.framework.api.context.InitializingComp;
import com.feige.utils.spi.annotation.SpiComp;

@SpiComp(interfaces = SerializedClassInit.class)
public class ProtoBufSerializedClassInit implements SerializedClassInit, InitializingComp {
    
    
    @Inject
    private SerializedClassManager serializedClassManager;
    

    @Override
    public void initialize() {
        generateClass();
    }
    
    private void generateClass(){
        generateClass(HandshakeReq.TYPE, HandshakeReqProto.class, HandshakeReqProto.Builder.class);
        generateClass(HandshakeResp.TYPE, HandshakeRespProto.class, HandshakeRespProto.Builder.class);
        generateClass(FastConnectReq.TYPE, FastConnectReqProto.class, FastConnectReqProto.Builder.class);
        generateClass(FastConnectResp.TYPE, FastConnectRespProto.class, FastConnectRespProto.Builder.class);
        generateClass(BindClientReq.TYPE, BindClientReqProto.class, BindClientReqProto.Builder.class);
        generateClass(ErrorResp.TYPE, ErrorRespProto.class, ErrorRespProto.Builder.class);
    }
    
    private <T extends Msg> void generateClass(Class<T> type, Object... args){
        serializedClassManager.getClass(ProtocolConst.PROTOCOL_BUFFER, type, args == null || args.length == 0 ? null : () -> args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initialize();
    }
}
