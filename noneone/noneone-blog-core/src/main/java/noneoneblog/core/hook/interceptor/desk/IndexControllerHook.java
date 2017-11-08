package noneoneblog.core.hook.interceptor.desk;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noneoneblog.core.hook.interceptor.InterceptorHookSupport;
import noneoneblog.core.hook.interceptor.InterceptorHookSupport.InterceptorListener;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * IndexController拦截钩子
 *
 * @author leisure
 */
@Component
public class IndexControllerHook extends InterceptorHookSupport {

    @Override
    public void init() {
        this.plugins = getPlugins(IndexControllerInterceptorListener.class);
    }

    @Override
    public String[] getInterceptor() {
        String[] n = {"noneoneblog.web.controller.desk.IndexController"};
        return n;
    }

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, ModelAndView modelAndView) throws Exception {

//        System.out.println(handler.getMethod().getName());
//
//        System.out.println(modelAndView);
//
//        System.out.println("");

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) throws Exception {

    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception {

    }

    public interface IndexControllerInterceptorListener extends InterceptorListener {

    }

}
