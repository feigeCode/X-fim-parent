package com.feige.api.bind;

import com.feige.api.constant.ClientType;

import java.util.Map;

/**
 * @author feige<br />
 * @ClassName: ClientBindManager <br/>
 * @Description: <br/>
 * @date: 2023/5/27 10:46<br/>
 */
public interface ClientBindManager {

    /**
     * register client bind info
     * @param clientBindInfo client bind info
     */
    void register(ClientBindInfo clientBindInfo);

    /**
     * find client bind info by clientId and client type
     * @param clientId client id
     * @param clientType client type
     * @return client bind info
     */
    ClientBindInfo lookup(String clientId, ClientType clientType);

    /**
     * find all client bind info by clientId
     * @param clientId client id
     * @return all client bind info
     */
    Map<String, ClientBindInfo> lookupAll(String clientId);


    /**
     * unregister client bind info
     * @param clientId client id
     * @param clientType client type
     * @return client bind info
     */
    ClientBindInfo unregister(String clientId, ClientType clientType);

    
}
