package com.example.refresh.web;

import com.example.refresh.service.DemoService;
import com.example.refresh.support.RefreshBean;
import com.example.refresh.support.RefreshBeanRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RefreshAutowired
@RestController
public class DemoController {

    @Autowired
    private RefreshBeanRegister scopeConfig;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private RefreshBean refreshBean;

    @Autowired
    private DemoService demoService;

    @RequestMapping("/refresh")
    public Mono<Void> test(){
        System.out.println(refreshBean);
        demoService.demo();
        System.out.println(refreshBean);
        return Mono.empty();
    }

    @RequestMapping("/test")
    public Mono<Void> test1(){
        System.out.println("#########################");
        return Mono.empty();
    }

}
