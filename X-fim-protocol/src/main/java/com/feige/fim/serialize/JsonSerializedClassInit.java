package com.feige.fim.serialize;

import com.feige.api.constant.ProtocolConst;
import com.feige.api.msg.BindClientReq;
import com.feige.api.msg.FastConnectReq;
import com.feige.api.msg.FastConnectResp;
import com.feige.api.msg.HandshakeReq;
import com.feige.api.msg.HandshakeResp;
import com.feige.api.msg.Msg;
import com.feige.api.serialize.SerializedClassManager;

public class JsonSerializedClassInit {
    
    private final SerializedClassManager serializedClassManager;

    public JsonSerializedClassInit(SerializedClassManager serializedClassManager) {
        this.serializedClassManager = serializedClassManager;
    }

    public void initialize() throws Exception {
            generateClass();
        
    }
    
    private void generateClass(){
        generateClass(HandshakeReq.TYPE);
        generateClass(HandshakeResp.TYPE);
        generateClass(FastConnectReq.TYPE);
        generateClass(FastConnectResp.TYPE);
        generateClass(BindClientReq.TYPE);
    }
    
    private <T extends Msg> void generateClass(Class<T> type, Object... args){
        serializedClassManager.getClass(ProtocolConst.JSON, type, args == null || args.length == 0 ? null : () -> args);
    }
}
