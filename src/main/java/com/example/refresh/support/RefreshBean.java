package com.example.refresh.support;

import lombok.Data;
import org.springframework.util.function.SingletonSupplier;

@Data
public class RefreshBean {

    private String name;

    private RefreshLevel refreshLevel;

    private boolean isAlive = false;

    public static final SingletonSupplier<RefreshBean> EMPTY = SingletonSupplier.of(RefreshBean::new);

    private SingletonSupplier singletonSupplier;

    //给默认BeanDefinition初始化用
    protected RefreshBean (){}

    public RefreshBean(String name,RefreshLevel refreshLevel,SingletonSupplier singletonSupplier){
        this.name = name;
        this.refreshLevel = refreshLevel;
        this.singletonSupplier = singletonSupplier;
        isAlive = true;
    }

}
