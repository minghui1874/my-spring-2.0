package com.gupao.vip.spring.framework.beans;


/**
 * 用于事件监听
 */
public class BeanPostProcessor {


    public Object postProcessBeforeInitialization(Object bean, String beanName){

        return bean;

    }

    public Object postProcessAfterInitialization(Object bean, String beanName){


        return bean;

    }

}
