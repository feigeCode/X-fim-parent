package com.feige.api.msg;

import com.feige.api.annotation.MsgComp;


@MsgComp(classKey = 4)
public interface SuccessResp extends Msg {
    
    Class<SuccessResp> TYPE = SuccessResp.class;
    
    int getStatusCode();

    SuccessResp setStatusCode(int statusCode);
    
    String getExtra();
    
    SuccessResp setExtra(String extra);
}
