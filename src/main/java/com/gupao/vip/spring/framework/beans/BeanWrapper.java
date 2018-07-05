package com.gupao.vip.spring.framework.beans;

import com.gupao.vip.spring.framework.core.FactoryBean;

public class BeanWrapper extends FactoryBean {


    //还会用  观察者 模式
    //1、支持事件响应，会有一个监听
    private BeanPostProcessor postProcessor;



    private Object wrapperInstance;

    //原始的通过反射new出来，要把它包装起来，存起来
    private Object originalInstance;

    public BeanWrapper(Object wrapperInstance) {

        this.wrapperInstance = wrapperInstance;
        this.originalInstance = wrapperInstance;

    }


    public Object getWrappedInstance(){
        return this.wrapperInstance;
    }

    //返回代理以后的Class
    //可能会是这个  $Proxy0
    public Class<?> getWrapperClass(){
        return this.wrapperInstance.getClass();
    }


    public BeanPostProcessor getPostProcessor() {
        return postProcessor;
    }

    public void setPostProcessor(BeanPostProcessor postProcessor) {
        this.postProcessor = postProcessor;
    }


}
