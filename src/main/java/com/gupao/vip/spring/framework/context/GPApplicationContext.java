package com.gupao.vip.spring.framework.context;

import com.gupao.vip.spring.demo.mvc.action.MyAction;
import com.gupao.vip.spring.framework.annotation.Autowried;
import com.gupao.vip.spring.framework.annotation.Controller;
import com.gupao.vip.spring.framework.annotation.Service;
import com.gupao.vip.spring.framework.beans.BeanDefinition;
import com.gupao.vip.spring.framework.beans.BeanPostProcessor;
import com.gupao.vip.spring.framework.beans.BeanWrapper;
import com.gupao.vip.spring.framework.context.support.BeanDefinitionReader;
import com.gupao.vip.spring.framework.core.BeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GPApplicationContext implements BeanFactory {


    private String [] configLocations;


    private BeanDefinitionReader reader;


    //用来保存配置信息
    private Map<String , BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    //用来保证注册式单例的容器
    private Map<String, Object> beanCacheMap = new ConcurrentHashMap<>();


    //存储所有的被代理过的对象
    private Map<String, BeanWrapper> beanWrapperMap = new ConcurrentHashMap<>();



    public GPApplicationContext(String ... configLocations) {
        this.configLocations = configLocations;
        refresh();

    }

    public void refresh () {


        //定位
        this.reader = new BeanDefinitionReader(configLocations);

        //加载
        List<String> beanDefinitions = reader.loadRegistryBeanClasses();


        //注册
        doRegistry(beanDefinitions);


        //依赖注入（lazy-init = false）要执行依赖注入
        //在这里自动调用getBean方法
        doAutowried();


        MyAction myAction = (MyAction) this.getBean("myAction");
        System.out.println(myAction.query("MING"));




    }

    private void doAutowried() {


        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : this.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();

            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                getBean(beanName);
            }
        }

        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : this.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();

            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                populateBean(beanName, this.beanWrapperMap.get(beanName).getWrappedInstance());
            }
        }



    }


    public void populateBean(String beanName, Object instance){

        Class<?> clazz = instance.getClass();

        //不是所有的Bean都需要注入
        if (!(clazz.isAnnotationPresent(Controller.class)
                || clazz.isAnnotationPresent(Service.class))) {
            return;
        }


        Field [] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (!field.isAnnotationPresent(Autowried.class)) {continue;}

            Autowried autowried = field.getAnnotation(Autowried.class);

            String autowriedBeanName = autowried.value().trim();

            if ("".equals(autowriedBeanName)) {
                autowriedBeanName = field.getName();
            }

            field.setAccessible(true);

            try {
                field.set(instance, this.beanWrapperMap.get(autowriedBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }



    }

    //真正的将BeanDefinitions注册到BeanDefinitionMap中
    private void doRegistry(List<String> beanDefinitions) {


        try {
            for (String className : beanDefinitions) {

                //beanName有三种情况
                //1、默认是类名首字母小写
                //2、自定义名字
                //3、接口注入


                Class<?> beanClass = Class.forName(className);

                //如果是一个接口，是不能实例化的
                //用他的实现类来实例化
                if (beanClass.isInterface()){continue;}

                BeanDefinition beanDefinition = reader.registerBean(className);
                if (beanDefinition != null) {
                    this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
                }

                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> i : interfaces) {
                    //如果是多个实现类，只能覆盖
                    //这个时候可以自定义名字
                    this.beanDefinitionMap.put(i.getName(), beanDefinition);
                }


                //到这里为止，容器初始化完毕

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //依赖注入，从这里开始，通过读取BeanDefinition中的信息
    //然后通过反射机制创建一个实例并返回
    //spring的做法是，不会把最原始的对象放进去，会用一个BeanWrapper来进行一次包装
    //装饰器模式：
    //1、保留原来的OOP关系
    //2、需要对它进行扩展，增强（为了以后AOP打基础）
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);

        String className = beanDefinition.getBaneClassName();

        try {

            //生成通知事件
            BeanPostProcessor beanPostProcessor = new BeanPostProcessor();

            Object instance = instantionBean(beanDefinition);
            if (instance == null) {return null;}

            //在实例初始化之前调用一次
            beanPostProcessor.postProcessBeforeInitialization(instance, beanName);

            BeanWrapper beanWrapper = new BeanWrapper(instance);
            beanWrapper.setPostProcessor(beanPostProcessor);
            this.beanWrapperMap.put(beanName, beanWrapper);

            //在实例初始化之后调用一次
            beanPostProcessor.postProcessAfterInitialization(instance, beanName);

            //通过这样一调用，相当于给我们自己留有了可操作的空间
            return this.beanWrapperMap.get(beanName).getWrappedInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


    //传一个BeanDefinition，就返回一个实例Bean
    private Object instantionBean(BeanDefinition beanDefinition) {


        Object instance = null;
        String className = beanDefinition.getBaneClassName();

        try {

            synchronized (beanDefinition) {

                //因为根据class才能确定一个类是否有实例
                if (this.beanCacheMap.containsKey(className)){
                    instance = this.beanCacheMap.get(className);
                } else {
                    Class<?> clazz = Class.forName(className);
                    instance = clazz.newInstance();
                    this.beanCacheMap.put(className, instance);
                }

            }

            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;

    }
}
