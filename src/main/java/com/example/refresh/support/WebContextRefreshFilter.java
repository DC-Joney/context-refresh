package com.example.refresh.support;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class WebContextRefreshFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.create(monoSink->{
            AtomicBoolean initState = RefreshScopeConfig.refreshContext;
            if(initState.get()){
               WrappedLock.wrappedLock();
            }
            monoSink.success();
        }).then(chain.filter(exchange));
    }
}
