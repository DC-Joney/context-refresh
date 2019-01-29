package com.example.refresh.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.refresh.support.RefreshBeanRegister.REFRESH_BEAN_NAME;

//处理Spring context conatiner
@Component
public class RefreshContextHandler implements BeanFactoryAware, InitializingBean {

    private Set<String> dependentBeans = new CopyOnWriteArraySet<>();

    private Map<Class<?>, Map<String, ?>> methodCallCache = new ConcurrentHashMap<>(10);

    protected static final AtomicBoolean refreshState = new AtomicBoolean(false);

//    private RefreshBeanContext refreshBeanContext;

    private static final String DESTROY_SINGLETON_NAME = "removeSingleton";

    private DefaultListableBeanFactory beanRegistry;


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanRegistry = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public void afterPropertiesSet() {
        dependentBeans.addAll(Arrays.asList(beanRegistry.getDependentBeans(REFRESH_BEAN_NAME)));
    }

    /**
     * @param t -> who callable method
     */
    public  <T> void refresh(T t) {

        dependentBeans.addAll(Arrays.asList(beanRegistry.getDependentBeans(REFRESH_BEAN_NAME)));

        try {
            if (refreshState.compareAndSet(false, true)) {

                if (!methodCallCache.containsKey(t.getClass())) {
                    methodCallCache.putIfAbsent(t.getClass(), beanRegistry.getBeansOfType(t.getClass()));
                }

                Map<String, ?> beansNames = methodCallCache.get(t.getClass());

                if (beansNames == null) {
                    throw new RuntimeException("The call method of object is not find in context container");
                }

                dependentBeans.stream()
                        .filter(beansNames::containsKey)
                        .forEach(beanName -> {
                            throw new RuntimeException("The " + REFRESH_BEAN_NAME + " is dose not dependentOn " + t.getClass());
                        });

                RefreshBean refreshBean = beanRegistry.getBean(RefreshBean.class);

                if (refreshBean != null) {

                    //将当前可用的refreshBean 失效
                    if (refreshBean.isAlive()) {
                        refreshBean.setAlive(false);
                    }

                    RefreshBean nextRefreshBean = beanRegistry.getBean(RefreshBeanContext.class).getAliveRefreshBean();

                    //必须保证nextRefreshBean不能为EMPTY，否则无意义
                    if(nextRefreshBean != RefreshBean.EMPTY.get()){

                        AtomicBoolean invokeMethodState = new AtomicBoolean(false);

                        ReflectionUtils.doWithMethods(DefaultListableBeanFactory.class, method -> {
                            try {
                                if (method.getName().equals(DESTROY_SINGLETON_NAME) && !invokeMethodState.get()) {
                                    method.setAccessible(true);
                                    method.invoke(beanRegistry, REFRESH_BEAN_NAME);
                                    invokeMethodState.set(true);
                                }
                            } catch (Exception e) {
                                throw new RuntimeException("The BeanFactory not support destroy singleton bean");
                            }
                        });

                        beanRegistry.getSingleton(REFRESH_BEAN_NAME, () -> nextRefreshBean);

                        dependentBeans
                                .forEach(beanName -> beanRegistry.configureBean(beanRegistry.getBean(beanName), beanName));
                    }
//                  beanRegistry.destroySingleton(REFRESH_BEAN_NAME);

                }

            }
        } finally {

            refreshState.set(false);

            WrappedLock.wrappedUnLock();
        }
    }


}
