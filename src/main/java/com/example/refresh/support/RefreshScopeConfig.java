package com.example.refresh.support;

import com.example.refresh.web.RefreshAutowired;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class RefreshScopeConfig implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private static final String REFRESH_BEAN_NAME = "refreshBean";

    private BeanDefinitionRegistry registry;

    private ApplicationContext applicationContext;

    public static final AtomicBoolean refreshContext = new AtomicBoolean(false);


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if(!registry.containsBeanDefinition(REFRESH_BEAN_NAME)){
            AnnotatedGenericBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(RefreshBean.class);
            beanDefinition.setLazyInit(true);
            beanDefinition.setPrimary(true);
            beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0,"111");
            beanDefinition.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_NO);
            registry.registerBeanDefinition(REFRESH_BEAN_NAME,beanDefinition);
        }
        this.registry = registry;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
    public void refresh(){
        if(refreshContext.compareAndSet(false,true)){
            if(registry.containsBeanDefinition(REFRESH_BEAN_NAME)){
                Object bean = applicationContext.getBean(REFRESH_BEAN_NAME);
                applicationContext.getAutowireCapableBeanFactory().destroyBean(bean);
                registry.removeBeanDefinition(REFRESH_BEAN_NAME);
            }
            AnnotatedGenericBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(RefreshBean.class);
            beanDefinition.setLazyInit(true);
            beanDefinition.setPrimary(true);
            beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0,"2222");
            beanDefinition.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_NO);
            registry.registerBeanDefinition(REFRESH_BEAN_NAME,beanDefinition);
//        AutowiredAnnotationBeanPostProcessor processor = applicationContext.getBean(AutowiredAnnotationBeanPostProcessor.class);

            //init refresh autowired
            applicationContext.getBeansWithAnnotation(RefreshAutowired.class);
        }
        refreshContext.set(false);
        WrappedLock.wrappedUnLock();
    }


}
