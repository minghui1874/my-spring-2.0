package com.gupao.vip.spring.demo.service.impl;

import com.gupao.vip.spring.demo.service.IDemoService;
import com.gupao.vip.spring.framework.annotation.Service;

@Service
public class DemoService implements IDemoService {


    @Override
    public String get(String name) {
        return "My name is " + name;
    }


}
