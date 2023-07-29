package com.feige.fim.context;

import com.feige.fim.config.ClientConfig;


public class ClientContext {
    private static ClientConfig clientConfig;
    
    public static ClientConfig getClientConfig(){
        return clientConfig;
    }
    
    public static void setClientConfig(ClientConfig cf){
        clientConfig = cf;
    }
}
