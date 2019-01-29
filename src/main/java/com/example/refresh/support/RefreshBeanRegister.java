package com.example.refresh.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

//注入默认BeanDefinition
@Component
public class RefreshBeanRegister implements BeanDefinitionRegistryPostProcessor {

    protected static final String REFRESH_BEAN_NAME = "refreshBean";

    private BeanDefinitionRegistry registry;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (!registry.containsBeanDefinition(REFRESH_BEAN_NAME)) {
            GenericBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(RefreshBean.class);
            beanDefinition.setLazyInit(true);
            beanDefinition.setPrimary(true);
            beanDefinition.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_NO);
            registry.registerBeanDefinition(REFRESH_BEAN_NAME, beanDefinition);
        }
        this.registry = registry;
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.getBean(RefreshContextHandler.class).refresh(this);
    }

}
