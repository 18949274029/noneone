package noneoneblog.core.hook.interceptor.desk;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noneoneblog.core.hook.interceptor.InterceptorHookSupport;
import noneoneblog.core.hook.interceptor.InterceptorHookSupport.InterceptorListener;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * GroupVidewController拦截钩子
 *
 * @author leisure
 */
@Component
public class GroupVidewControllerHook extends InterceptorHookSupport {


    @Override
    public void init() {
        this.plugins = getPlugins(GroupViewControllerInterceptorListener.class);
    }

    @Override
    public String[] getInterceptor() {
        //说明要拦截的controller
        return new String[]{"noneoneblog.web.controller.desk.group.GroupVidewController"};
    }

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception {
        super.onPreHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, ModelAndView modelAndView) throws Exception {
        super.onPostHandle(request, response, handler, modelAndView);

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) throws Exception {
        super.onAfterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception {
        super.onAfterConcurrentHandlingStarted(request, response, handler);
    }

    public interface GroupViewControllerInterceptorListener extends InterceptorListener {

    }
}
