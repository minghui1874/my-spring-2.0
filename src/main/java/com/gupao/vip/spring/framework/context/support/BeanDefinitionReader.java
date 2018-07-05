package com.gupao.vip.spring.framework.context.support;

import com.gupao.vip.spring.framework.beans.BeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


//用来对配置文件进行查找、读取以及解析
public class BeanDefinitionReader {


    private Properties config = new Properties();


    private List<String> registryBeanClasses = new ArrayList<>();



    //
    private final String SCAN_PACKAGE = "scanPackage";


    public BeanDefinitionReader(String ... locations) {

        for (String location : locations) {

            //在spring中是通过Reader去查找和定位的
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(location.replace("classpath:", ""))) {
                config.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }

            doScanner(config.getProperty(SCAN_PACKAGE));

        }

    }


    public List<String> loadRegistryBeanClasses(){


        return  this.registryBeanClasses;
    }


    /**
     * 每注册一个className，就返回一个BeanDefinition，
     * @param className
     * @return
     */
    public BeanDefinition registerBean(String className){

        if (this.registryBeanClasses.contains(className)) {
            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setBaneClassName(className);
            beanDefinition.setFactoryBeanName(lowerFirstCase(className.substring(className.lastIndexOf(".")+1)));
            return beanDefinition;
        }


        return null;


    }



    public Properties getConfig(){

        return this.config;
    }


    /**
     * 递归扫描所有的相关联的class
     * @param packageName
     */
    private void doScanner(String packageName) {
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));

        File classDir = new File(url.getFile());

        for (File file : classDir.listFiles()) {
            if (file.isDirectory()) {
                doScanner(packageName + "." + file.getName());
            } else {
                registryBeanClasses.add(packageName + "." + file.getName().replace(".class",""));
            }
        }

    }


    private String lowerFirstCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }


}
