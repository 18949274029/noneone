
package noneoneblog.web.controller.admin;

import java.util.List;

import noneoneblog.base.data.Data;
import noneoneblog.core.data.Comment;
import noneoneblog.core.persist.service.CommentService;
import noneoneblog.web.controller.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author leisure
 *
 */
@Controller("mng_comment_ctl")
@RequestMapping("/admin/comments")
public class CommentsController extends BaseController {
	@Autowired
	private CommentService commentService;
	
	@RequestMapping("/list")
	public String list(ModelMap model) {
		Pageable pageable = wrapPageable();
		Page<Comment> page = commentService.paging4Admin(pageable);
		model.put("page", page);
		return "/admin/comments/list";
	}
	
	@RequestMapping("/delete")
	public @ResponseBody
	Data delete(@RequestParam("id") List<Long> id) {
		Data data = Data.failure("操作失败");
		if (id != null) {
			try {
				commentService.delete(id);
				data = Data.success("操作成功", Data.NOOP);
			} catch (Exception e) {
				data = Data.failure(e.getMessage());
			}
		}
		return data;
	}
}
