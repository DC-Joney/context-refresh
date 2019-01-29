package com.example.refresh.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.RequestPath;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * refresh当前容器
 */
@Order(Integer.MIN_VALUE)
@Component
public class RefreshContextFilter implements WebFilter {

    @Autowired
    private RefreshContextHandler handler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.just(exchange.getRequest().getPath())
                .filter(requestPath -> requestPath.value().equals("/refresh"))
                .<RequestPath>handle((requestPath, synchronousSink) -> {
                    handler.refresh(this);
                    synchronousSink.next(requestPath);
                }).then(chain.filter(exchange));
    }
}
