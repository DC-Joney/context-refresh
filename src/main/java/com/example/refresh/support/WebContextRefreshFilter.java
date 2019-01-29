package com.example.refresh.support;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class WebContextRefreshFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.just(RefreshScopeConfig.refreshContext)
                .handle(this::conditionRefresh)
                .flatMap(s-> chain.filter(exchange));
    }

    private void conditionRefresh(AtomicBoolean initState, SynchronousSink<AtomicBoolean> handleSink){
        if(initState.get()){
            WrappedLock.wrappedLock();
        }
        handleSink.next(initState);
    }

}
