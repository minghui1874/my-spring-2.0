package com.gupao.vip.spring.demo.mvc.action;

import com.gupao.vip.spring.framework.webmvc.GPModelAndView;
import com.gupao.vip.spring.demo.service.IDemoService;
import com.gupao.vip.spring.framework.annotation.Autowried;
import com.gupao.vip.spring.framework.annotation.Controller;
import com.gupao.vip.spring.framework.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MyAction {

    @Autowried
    private IDemoService demoService;

    @RequestMapping("index.html")
    public GPModelAndView query (String name) {
        return demoService.get(name);
    }

}
