
package noneoneblog.web.controller.api;

import javax.servlet.http.HttpServletRequest;

import noneoneblog.base.lang.Consts;
import noneoneblog.core.biz.PostBiz;
import noneoneblog.core.data.Post;
import noneoneblog.web.controller.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author leisure
 *
 */
@Controller
@RequestMapping("/api")
public class PostJsonController extends BaseController {
	@Autowired
	private PostBiz postBiz;
	
	@RequestMapping("/posts.json")
	public @ResponseBody
	Page<Post> posts(HttpServletRequest request) {
		String order = ServletRequestUtils.getStringParameter(request, "ord", Consts.order.NEWEST);
		int gid = ServletRequestUtils.getIntParameter(request, "gid", 0);
		Pageable pageable = wrapPageable();
		Page<Post> page = postBiz.paging(pageable, gid, order);
		
		return page;
	}
}
