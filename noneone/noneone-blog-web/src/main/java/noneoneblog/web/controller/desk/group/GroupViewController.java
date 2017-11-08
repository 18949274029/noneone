
package noneoneblog.web.controller.desk.group;

import noneoneblog.core.biz.PostBiz;
import noneoneblog.core.data.Post;
import noneoneblog.core.persist.service.PostService;
import noneoneblog.web.controller.BaseController;
import noneoneblog.web.controller.desk.Views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 文章浏览
 * 
 * @author leisure
 * 
 */
@Controller
@RequestMapping("/view")
public class GroupViewController extends BaseController {
	@Autowired
	private PostBiz postBiz;
	@Autowired
	private PostService postService;

	@RequestMapping("/{id}")
	public String view(@PathVariable Long id, ModelMap model) {
		Post ret = postBiz.getPost(id);
		
		Assert.notNull(ret, "该文章已被删除");
		
		postService.identityViews(id);
		model.put("ret", ret);
		return getView(Views.ROUTE_POST_VIEW);
	}
}
