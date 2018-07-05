package com.gupao.vip.spring.framework.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GPHandlerAdapter {

    /**
     *
     * @param req
     * @param resp  传入response的意义：只是为了将其赋值给方法的参数
     * @param handler 传入handler的原因，因为handler中包含了Controller、method、url信息
     * @return
     */

    public GPModelAndView handle(HttpServletRequest req, HttpServletResponse resp, GPHandlerMapping handler) {


        //根据用户请求的参数信息，跟method中的参数信息，要进行动态匹配
        //只有当用户传过来的ModelAndView为空的时候，才会new一个默认的

        return null;

    }
}
