package com.gupao.vip.spring.framework.core;

public interface BeanFactory {


    /**
     * 根据beanName从IOC容器中获取一个实例
     * @param beanName
     * @return
     */
    Object getBean(String beanName);



}
