
package noneoneblog.web.controller.desk.ta;

import noneoneblog.core.biz.PostBiz;
import noneoneblog.core.data.Post;
import noneoneblog.core.data.User;
import noneoneblog.core.persist.service.UserService;
import noneoneblog.web.controller.BaseController;
import noneoneblog.web.controller.desk.Views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 访问他人主页
 * @author leisure
 *
 */
@Controller
public class TaController extends BaseController {
	@Autowired
	private PostBiz postBiz;
	@Autowired
	private UserService userService;
	
	@RequestMapping("/ta/{uid}")
	public String home(@PathVariable Long uid, ModelMap model) {
		User user = userService.get(uid);
		Pageable pageable = wrapPageable();
		Page<Post> page = postBiz.pagingByAuthorId(pageable, uid);
		
		model.put("user", user);
		model.put("page", page);
		return getView(Views.TA_HOME);
	}
}
