
package noneoneblog.web.controller.api;

import java.util.List;

import noneoneblog.base.data.Data;
import noneoneblog.core.biz.PostBiz;
import noneoneblog.core.data.AccountProfile;
import noneoneblog.core.data.Post;
import noneoneblog.core.data.User;
import noneoneblog.core.persist.service.UserService;
import noneoneblog.web.controller.BaseController;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 侧边栏数据加载
 * 
 * @author leisure
 *
 */
@Controller
@RequestMapping("/api")
public class SidebarController extends BaseController {
	@Autowired
	private PostBiz postBiz;

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody
	Data login(String username, String password, ModelMap model) {
		Data data = Data.failure("操作失败");

		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return data;
		}

		AuthenticationToken token = createToken(username, password);
		if (token == null) {
			data.setMessage("用户名或密码错误");
			return data;
		}

		try {
			SecurityUtils.getSubject().login(token);
			data = Data.success("登录成功", getSubject().getProfile());

		} catch (AuthenticationException e) {
			if (e instanceof UnknownAccountException) {
				data.setMessage("用户不存在");
			} else if (e instanceof LockedAccountException) {
				data.setMessage("用户被禁用");
			} else {
				data.setMessage("用户认证失败");
			}
		}
		return data;
	}

	@RequestMapping("/latests.json")
	public @ResponseBody List<Post> latests() {
		AccountProfile up = getSubject().getProfile();
		long ignoreUserId = 0;
		if (up != null) {
			ignoreUserId = up.getId();
		}
		List<Post> rets = postBiz.findRecents(6, ignoreUserId);
		return rets;
	}
	
	@RequestMapping("/hots.json")
	public @ResponseBody List<Post> hots() {
		AccountProfile up = getSubject().getProfile();
		long ignoreUserId = 0;
		if (up != null) {
			ignoreUserId = up.getId();
		}
		List<Post> rets = postBiz.findHots(6, ignoreUserId);
		return rets;
	}
	
	/**
	 * 热门用户
	 * @param pn
	 * @return
	 */
	@RequestMapping(value="/hotusers.json")
	public @ResponseBody List<User> hotusers(Integer pn) {
		List<User> rets = userService.findHotUserByfans();
         return rets;
	}
}
