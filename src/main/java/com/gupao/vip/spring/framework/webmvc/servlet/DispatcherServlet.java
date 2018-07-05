package com.gupao.vip.spring.framework.webmvc.servlet;

import com.gupao.vip.spring.framework.context.GPApplicationContext;
import com.gupao.vip.spring.framework.webmvc.GPHandlerAdapter;
import com.gupao.vip.spring.framework.webmvc.GPHandlerMapping;
import com.gupao.vip.spring.framework.webmvc.GPModelAndView;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//Servlet只是一个MVC的启动入口
public class DispatcherServlet extends HttpServlet {


    private final String LOCATION = "contextConfigLocation";

    private List<GPHandlerMapping> handlerMappings = new ArrayList<>();

    private List<GPHandlerAdapter> handlerAdapters = new ArrayList<>();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


//        String url = req.getRequestURI();
//        String contextPath = req.getContextPath();
//        url = url.replace(contextPath, "").replaceAll("/+", "/");
//        GPHandlerMapping handler = handlerMappings.get(url);


    }

    protected void dpDispatch(HttpServletRequest req, HttpServletResponse resp) {

        GPHandlerMapping handler = getHandler(req);


        GPHandlerAdapter handlerAdapter = getHandlerAdapter(handler);

        GPModelAndView modelAndView = handlerAdapter.handle(req, resp, handler);

        processDispatchResult(resp, modelAndView);

    }

    private void processDispatchResult(HttpServletResponse resp, GPModelAndView modelAndView) {

        //调用viewResolver的resolveView方法



    }

    private GPHandlerAdapter getHandlerAdapter(GPHandlerMapping handler) {


        return null;
    }

    private GPHandlerMapping getHandler(HttpServletRequest req) {

        return null;
    }


    @Override
    public void init(ServletConfig config) throws ServletException {

        //相当于已经初始化了IOC容器
        GPApplicationContext context = new GPApplicationContext(config.getInitParameter(LOCATION));

        initStrategies(context);


    }

    protected void initStrategies(GPApplicationContext context) {
        initMultipartResolver(context);
        initLocaleResolver(context);
        initThemeResolver(context);


        //用来保存controller中配置的requestMapping和method的一个对应关系
        initHandlerMappings(context);

        //用来动态匹配method参数，包括类型转换、动态赋值
        initHandlerAdapters(context);

        initHandlerExceptionResolvers(context);
        initRequestToViewNameTranslator(context);

        //通过viewResolvers实现动态模板解析
        initViewResolvers(context);
        initFlashMapManager(context);
    }


    private void initViewResolvers(GPApplicationContext context) {


    }


    private void initHandlerAdapters(GPApplicationContext context) {


    }

    /**
     * 将Controller中配置的RequestMapping和method进行一一对应
     *
     * @param context
     */
    private void initHandlerMappings(GPApplicationContext context) {

        //按照通常理解应该是一个Map
        //Map<String, Method> map
        //map.put(url, Method)


    }


    private void initHandlerExceptionResolvers(GPApplicationContext context) {
    }

    private void initThemeResolver(GPApplicationContext context) {
    }

    private void initLocaleResolver(GPApplicationContext context) {
    }

    private void initMultipartResolver(GPApplicationContext context) {
    }

    private void initFlashMapManager(GPApplicationContext context) {
    }

    private void initRequestToViewNameTranslator(GPApplicationContext context) {
    }


}
