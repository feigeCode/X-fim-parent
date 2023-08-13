package com.feige.api.msg;

import com.feige.api.annotation.MsgComp;


@MsgComp(classKey = 4)
public interface FastConnectResp extends Msg {
    
    Class<FastConnectResp> TYPE = FastConnectResp.class;
    
    int getStatusCode();

    FastConnectResp setStatusCode(int statusCode);
}
