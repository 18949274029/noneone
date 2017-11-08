
package noneoneblog.web.controller.desk.user;

import noneoneblog.core.data.AccountProfile;
import noneoneblog.core.data.Comment;
import noneoneblog.core.data.Favor;
import noneoneblog.core.data.Feeds;
import noneoneblog.core.data.Notify;
import noneoneblog.core.data.Post;
import noneoneblog.core.data.User;
import noneoneblog.core.persist.service.CommentService;
import noneoneblog.core.persist.service.FavorService;
import noneoneblog.core.persist.service.FeedsService;
import noneoneblog.core.persist.service.FollowService;
import noneoneblog.core.persist.service.NotifyService;
import noneoneblog.core.persist.service.PostService;
import noneoneblog.core.persist.service.UserService;
import noneoneblog.shiro.authc.AccountSubject;
import noneoneblog.web.controller.BaseController;
import noneoneblog.web.controller.desk.Views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户主页
 * @author leisure
 *
 */
@Controller
public class HomeController extends BaseController {
	@Autowired
	private PostService postService;
	@Autowired
	private FeedsService feedsService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private UserService userService;
	@Autowired
	private FollowService followService;
	@Autowired
	private FavorService favorService;
	@Autowired
	private NotifyService notifyService;

	/**
	 * 用户主页
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home")
	public String home(ModelMap model) {
		Pageable pageable = wrapPageable();
		AccountSubject subject = getSubject();

		Page<Feeds> page = feedsService.findUserFeeds(pageable, subject.getProfile().getId());

		model.put("page", page);
		initUser(model);

		return getView(Views.HOME_FEEDS);
	}

	/**
	 * 我发布的文章
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home", params = "method=posts")
	public String posts(ModelMap model) {
		Pageable pageable = wrapPageable();
		AccountProfile up = getSubject().getProfile();
		Page<Post> page = postService.pagingByAuthorId(pageable, up.getId());

		model.put("page", page);
		initUser(model);

		return getView(Views.HOME_POSTS);
	}

	/**
	 * 我发表的评论
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home", params = "method=comments")
	public String comments(ModelMap model) {
		Pageable pageable = wrapPageable();
		AccountSubject subject = getSubject();
		Page<Comment> page = commentService.paging4Home(pageable, subject.getProfile().getId());

		model.put("page", page);
		initUser(model);

		return getView(Views.HOME_COMMENTS);
	}

	/**
	 * 我喜欢过的文章
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home/favors")
	public String favors(ModelMap model) {
		Pageable pageable = wrapPageable();
		AccountProfile profile = getSubject().getProfile();
		Page<Favor> page = favorService.pagingByOwnId(pageable, profile.getId());

		model.put("page", page);
		initUser(model);

		return getView(Views.HOME_FAVORS);
	}

	/**
	 * 我的关注
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home/follows")
	public String follows(ModelMap model) {
		Pageable pageable = wrapPageable();
		AccountProfile profile = getSubject().getProfile();
		Page<User> page = followService.follows(pageable, profile.getId());

		model.put("page", page);
		initUser(model);

		return getView(Views.HOME_FOLLOWS);
	}

	/**
	 * 我的粉丝
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home/fans")
	public String fans(ModelMap model) {
		Pageable pageable = wrapPageable();
		AccountProfile profile = getSubject().getProfile();
		Page<User> page = followService.fans(pageable, profile.getId());

		model.put("page", page);
		initUser(model);

		return getView(Views.HOME_FANS);
	}

	/**
	 * 我的通知
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home/notifies")
	public String notifies(ModelMap model) {
		Pageable pageable = wrapPageable();
		AccountProfile profile = getSubject().getProfile();
		Page<Notify> page = notifyService.findByOwnId(pageable, profile.getId());
		// 标记已读
		notifyService.readed4Me(profile.getId());

		model.put("page", page);
		initUser(model);

		pushBadgesCount();

		return getView(Views.HOME_NOTIFIES);
	}

	private void initUser(ModelMap model) {
		AccountProfile up = getSubject().getProfile();
		User user = userService.get(up.getId());

		model.put("user", user);
	}

	private void pushBadgesCount() {
		AccountProfile profile = (AccountProfile) session.getAttribute("profile");
		if (profile != null && profile.getBadgesCount() != null) {
			profile.getBadgesCount().setNotifies(0);
			session.setAttribute("profile", profile);
		}
	}
}
