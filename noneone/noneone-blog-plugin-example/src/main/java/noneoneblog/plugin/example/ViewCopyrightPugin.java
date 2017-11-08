package noneoneblog.plugin.example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noneoneblog.core.data.Post;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import noneoneblog.core.hook.interceptor.desk.GroupVidewControllerHook;
/**
 * @author leisure
 */
@Component
public class ViewCopyrightPugin implements GroupVidewControllerHook.GroupViewControllerInterceptorListener {
    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, ModelAndView modelAndView) throws Exception {
        Post ret = (Post) modelAndView.getModelMap().get("ret");
        String content = ret.getContent();
        if (!content.contains("本文归作者所有，未经作者允许，不得转载")){
            content += "<br/>注意：本文归作者所有，未经作者允许，不得转载";
            ret.setContent(content);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) throws Exception {

    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception {

    }
}
