package com.feige.gateway.rule;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.Server;

/**
 * @author feige<br />
 * @ClassName: ConsistenceHashRule <br/>
 * @Description: <br/>
 * @date: 2021/12/20 17:06<br/>
 */
public class ConsistenceHashRule extends AbstractLoadBalancerRule {
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object key) {
        return null;
    }
}
