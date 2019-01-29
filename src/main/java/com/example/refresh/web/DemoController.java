package com.example.refresh.web;

import com.example.refresh.support.RefreshBean;
import com.example.refresh.support.RefreshScopeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RefreshAutowired
@RestController
public class DemoController {

    @Autowired
    private RefreshScopeConfig scopeConfig;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private RefreshBean refreshBean;

    @RequestMapping("/refresh")
    public Mono<Void> test(){
        System.out.println(refreshBean);
        scopeConfig.refresh();
        System.out.println(refreshBean);
        return Mono.empty();
    }

    @RequestMapping("/test")
    public Mono<Void> test1(){
        System.out.println("#########################");
        return Mono.empty();
    }

}
