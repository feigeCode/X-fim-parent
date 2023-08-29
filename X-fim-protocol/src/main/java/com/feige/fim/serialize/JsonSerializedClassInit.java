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
import com.feige.framework.annotation.Inject;
import com.feige.framework.api.context.InitializingComp;
import com.feige.utils.spi.annotation.SpiComp;


@SpiComp(interfaces = SerializedClassInit.class)
public class JsonSerializedClassInit implements SerializedClassInit, InitializingComp {
    
    @Inject
    private SerializedClassManager serializedClassManager;

    @Override
    public void initialize() {
            generateClass();
        
    }
    
    private void generateClass(){
        generateClass(HandshakeReq.TYPE);
        generateClass(HandshakeResp.TYPE);
        generateClass(FastConnectReq.TYPE);
        generateClass(FastConnectResp.TYPE);
        generateClass(BindClientReq.TYPE);
        generateClass(ErrorResp.TYPE);
    }
    
    private <T extends Msg> void generateClass(Class<T> type, Object... args){
        serializedClassManager.getClass(ProtocolConst.JSON, type, args == null || args.length == 0 ? null : () -> args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initialize();
    }
}
