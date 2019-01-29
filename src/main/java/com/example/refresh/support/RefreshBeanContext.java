package com.example.refresh.support;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.function.SingletonSupplier;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//测试类
@Component
public class RefreshBeanContext implements InitializingBean {

    private List<RefreshBean> refreshBeans = new CopyOnWriteArrayList<>();


    public RefreshBean getAliveRefreshBean(){
        return refreshBeans.stream()
                .filter(RefreshBean::isAlive)
                .findFirst().orElse(RefreshBean.EMPTY.get());
    }


    public void registerRefreshBean(RefreshBean refreshBean){
        refreshBeans.add(refreshBean);
    }


    @Override
    public void afterPropertiesSet() {
        registerRefreshBean(new RefreshBean("demo1",RefreshLevel.MASTER, SingletonSupplier.of(ModelExample::new)));
        registerRefreshBean(new RefreshBean("demo2",RefreshLevel.SLAVE, SingletonSupplier.of(ModelExample::new)));
    }
}
