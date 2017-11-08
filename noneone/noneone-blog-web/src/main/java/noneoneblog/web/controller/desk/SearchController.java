
package noneoneblog.web.controller.desk;

import noneoneblog.core.data.Post;
import noneoneblog.core.persist.service.PostService;
import noneoneblog.web.controller.BaseController;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 文章搜索
 * @author leisure
 *
 */
@Controller
public class SearchController extends BaseController {
	@Autowired
	private PostService postService;

	@RequestMapping("/search")
	public String search(String q, ModelMap model) {
		Pageable pageable = wrapPageable();
		try {
			if (StringUtils.isNotEmpty(q)) {
				Page<Post> page = postService.search(pageable, q);
				model.put("page", page);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("q", q);
		return getView(Views.BROWSE_SEARCH);
	}
	
}
