package com.feige.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author feige<br />
 * @ClassName: WsReactiveLoadBalancerClientFilter <br/>
 * @Description: <br/>
 * @date: 2021/12/20 17:33<br/>
 */
public class    WsReactiveLoadBalancerClientFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
