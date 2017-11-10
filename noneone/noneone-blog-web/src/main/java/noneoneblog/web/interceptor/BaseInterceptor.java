
package noneoneblog.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noneoneblog.base.context.AppContext;
import noneoneblog.base.context.Global;
import noneoneblog.core.hook.interceptor.InterceptorHookManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 
 * 基础拦截器 - 向 request 中添加一些基础变量
 * 
 * @author leisure
 * 
 */
public class BaseInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private InterceptorHookManager interceptorHookManager;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		interceptorHookManager.preHandle(request, response, handler);
		return true;
	}
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		request.setAttribute("base", request.getContextPath());
		interceptorHookManager.postHandle(request,response,handler,modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		super.afterCompletion(request, response, handler, ex);
		interceptorHookManager.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		super.afterConcurrentHandlingStarted(request, response, handler);
		interceptorHookManager.afterConcurrentHandlingStarted(request, response, handler);
	}

}
