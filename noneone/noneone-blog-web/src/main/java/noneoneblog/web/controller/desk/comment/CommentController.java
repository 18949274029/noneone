/**
 * 
 */
package noneoneblog.web.controller.desk.comment;

import javax.servlet.http.HttpServletRequest;

import noneoneblog.base.data.Data;
import noneoneblog.base.lang.Consts;
import noneoneblog.core.data.AccountProfile;
import noneoneblog.core.data.Comment;
import noneoneblog.core.event.NotifyEvent;
import noneoneblog.core.persist.service.CommentService;
import noneoneblog.web.controller.BaseController;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

/**
 * @author leisure
 *
 */
@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
	@Autowired
	private CommentService commentService;
	@Autowired
	private ApplicationContext applicationContext;

	@RequestMapping("/list/{toId}")
	public @ResponseBody Page<Comment> view(@PathVariable Long toId) {
		Pageable pageable = wrapPageable();
		Page<Comment> page = commentService.paging(pageable, toId);
		return page;
	}
	
	@RequestMapping("/submit")
	public @ResponseBody
	Data post(Long toId, String text, HttpServletRequest request) {
		Data data = Data.failure("操作失败");
		logger.info("评论开始");
		long pid = ServletRequestUtils.getLongParameter(request, "pid", 0);
		
//		if (!SecurityUtils.getSubject().isAuthenticated()) {
//			data = Data.failure("请先登录在进行操作");
//			
//			return data;
//		}
		if (toId > 0 && StringUtils.isNotEmpty(text)) {
			AccountProfile up = getSubject().getProfile();
			
			Comment c = new Comment();
			c.setToId(toId);
			c.setContent(HtmlUtils.htmlEscape(text));
			c.setAuthorId(up.getId());
			
			c.setPid(pid);
			
			commentService.post(c);

            if(toId != up.getId()) {
			    sendNotify(up.getId(), toId, pid);
            }
			
			data = Data.success("发表成功!", Data.NOOP);
		}
		return data;
	}

	@RequestMapping("/delete")
	public @ResponseBody Data delete(Long id) {
		Data data = Data.failure("操作失败");
		if (id != null) {
			AccountProfile up = getSubject().getProfile();
			try {
				commentService.delete(id, up.getId());
				data = Data.success("操作成功", Data.NOOP);
			} catch (Exception e) {
				data = Data.failure(e.getMessage());
			}
		}
		return data;
	}

	/**
	 * 发送通知
	 * @param userId
	 * @param postId
	 */
	private void sendNotify(long userId, long postId, long pid) {
		NotifyEvent event = new NotifyEvent("NotifyEvent");
		event.setFromUserId(userId);

		if (pid > 0) {
			event.setEvent(Consts.NOTIFY_EVENT_COMMENT_REPLY);
		} else {
			event.setEvent(Consts.NOTIFY_EVENT_COMMENT);
		}
		// 此处不知道文章作者, 让通知事件系统补全
		event.setPostId(postId);
		applicationContext.publishEvent(event);
	}
}