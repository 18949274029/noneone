
package noneoneblog.web.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import noneoneblog.base.utils.AESUtil;
import noneoneblog.core.data.Post;
import noneoneblog.web.controller.desk.Views;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;

/**
 * 登录页
 * @author leisure
 */
@Controller
public class LoginController extends BaseController {

    /**
     * 跳转登录页
     * @return
     */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String view() {
		return getView(Views.LOGIN);
	}

    /**
     * 提交登录
     * @param username
     * @param password
     * @param model
     * @return
     */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(String username, String password,@RequestParam(value = "rememberMe",defaultValue = "0") int rememberMe, ModelMap model) {
		String ret = getView(Views.LOGIN);
		
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return ret;
        }
		
		AuthenticationToken token = createToken(username, password);
        if (token == null) {
        	model.put("message", "用户名或密码错误");
            return ret;
        }

        if (rememberMe == 1) {
            ((UsernamePasswordToken) token).setRememberMe(true);
        }

        try {
            SecurityUtils.getSubject().login(token);

            ret = Views.REDIRECT_HOME;

            // 更新消息数量
//            pushBadgesCount();
        } catch (AuthenticationException e) {
            if (e instanceof UnknownAccountException) {
            	model.put("message", "用户不存在");
            } else if (e instanceof LockedAccountException) {
            	model.put("message", "用户被禁用");
            } else {
            	model.put("message", "用户认证失败");
            }
        }

        return ret;
	}

	
//    private void pushBadgesCount() {
//        new Thread(() -> {
//
//            try {
//                Thread.sleep(1 * Consts.TIME_MIN);
//            } catch (InterruptedException e) {
//            }
//
//            AccountSubject subject = (AccountSubject) SecurityUtils.getSubject();
//
//            BadgesCount count = new BadgesCount();
//            count.setNotifies(notifyService.unread4Me(subject.getProfile().getId()));
//            session.setAttribute("badgesCount", count);
//
//        }).start();
//    }

}
