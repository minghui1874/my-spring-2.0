package com.gupao.vip.spring.demo.mvc.action;


import com.gupao.vip.spring.demo.service.IDemoService;
import com.gupao.vip.spring.framework.annotation.Autowried;
import com.gupao.vip.spring.framework.annotation.Controller;
import com.gupao.vip.spring.framework.annotation.RequestMapping;
import com.gupao.vip.spring.framework.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/demo")
public class DemoAction {


    @Autowried
    private IDemoService demoService;


    @RequestMapping("/query.json")
    public void query(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("name") String name) {
        String result = demoService.get(name);
        System.out.println(result);
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("/edit.json")
    public void edit(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("id") Integer id) {


    }


}
