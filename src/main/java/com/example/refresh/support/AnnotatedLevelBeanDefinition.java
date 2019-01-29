package com.example.refresh.support;

import lombok.Data;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;

@Data
public class AnnotatedLevelBeanDefinition extends AnnotatedGenericBeanDefinition {

    private final RefreshLevel refreshLevel;

    public AnnotatedLevelBeanDefinition(Class<?> beanClass,RefreshLevel refreshLevel) {
        super(beanClass);
        this.refreshLevel = refreshLevel;
    }


}
