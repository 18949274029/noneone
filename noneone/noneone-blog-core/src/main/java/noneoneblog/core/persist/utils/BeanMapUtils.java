
package noneoneblog.core.persist.utils;

import java.util.ArrayList;
import java.util.List;

import noneoneblog.core.data.AccountProfile;
import noneoneblog.core.data.Attach;
import noneoneblog.core.data.AuthMenu;
import noneoneblog.core.data.Comment;
import noneoneblog.core.data.Favor;
import noneoneblog.core.data.Feeds;
import noneoneblog.core.data.FriendLink;
import noneoneblog.core.data.Game;
import noneoneblog.core.data.Group;
import noneoneblog.core.data.Notify;
import noneoneblog.core.data.Post;
import noneoneblog.core.data.Role;
import noneoneblog.core.data.User;
import noneoneblog.core.persist.entity.AttachPO;
import noneoneblog.core.persist.entity.AuthMenuPO;
import noneoneblog.core.persist.entity.CommentPO;
import noneoneblog.core.persist.entity.FavorPO;
import noneoneblog.core.persist.entity.FeedsPO;
import noneoneblog.core.persist.entity.FriendLinkPO;
import noneoneblog.core.persist.entity.GamePO;
import noneoneblog.core.persist.entity.GroupPO;
import noneoneblog.core.persist.entity.NotifyPO;
import noneoneblog.core.persist.entity.PostPO;
import noneoneblog.core.persist.entity.RolePO;
import noneoneblog.core.persist.entity.UserPO;

import org.springframework.beans.BeanUtils;

/**
 * @author langhsu
 *
 */
public class BeanMapUtils {
	public static String[] USER_IGNORE = new String[]{"password", "extend", "roles"};

	public static String[] POST_IGNORE_LIST = new String[]{"markdown", "content"};

	public static User copy(UserPO po, int level) {
		if (po == null) {
			return null;
		}
		User ret = new User();
		BeanUtils.copyProperties(po, ret, USER_IGNORE);
		
		if (level > 0) {
			List<RolePO> rolePOs = po.getRoles();
			List<Role> roles = new ArrayList<Role>();
			for(RolePO rolePo :rolePOs){
				Role role = copy(rolePo);
				roles.add(role);
			}
			ret.setRoles(roles);
		}
		return ret;
	}

	public static AccountProfile copyPassport(UserPO po) {
		AccountProfile passport = new AccountProfile(po.getId(), po.getUsername());
		passport.setName(po.getName());
		passport.setEmail(po.getEmail());
		passport.setAvatar(po.getAvatar());
		passport.setLastLogin(po.getLastLogin());
		passport.setStatus(po.getStatus());
		passport.setActiveEmail(po.getActiveEmail());

		List<AuthMenu> menus = new ArrayList<AuthMenu>();
		List<RolePO> rolePOs = po.getRoles();
		List<Role> roles = new ArrayList<Role>();
		for(RolePO rolePo :rolePOs){
			Role role = copy(rolePo);
			roles.add(role);
		}
		for(Role role : roles){
			List<AuthMenu> authMenus = role.getAuthMenus();
			menus.addAll(authMenus);
		}
		passport.setAuthMenus(menus);
		return passport;
	}

	public static Comment copy(CommentPO po) {
		Comment ret = new Comment();
		BeanUtils.copyProperties(po, ret);
		return ret;
	}

	public static Post copy(PostPO po, int level) {
		Post d = new Post();
		if (level > 0) {
			BeanUtils.copyProperties(po, d);
		} else {
			BeanUtils.copyProperties(po, d, POST_IGNORE_LIST);
		}
		return d;
	}

	public static Attach copy(AttachPO po) {
		Attach ret = new Attach();
		BeanUtils.copyProperties(po, ret);
		return ret;
	}

	public static Group copy(GroupPO po) {
		Group r = new Group();
		BeanUtils.copyProperties(po, r);
		return r;
	}

	public static AuthMenu copy(AuthMenuPO po) {
		AuthMenu am = new AuthMenu();
		BeanUtils.copyProperties(po, am, "children");
		return am;
	}

	public static Role copy(RolePO po) {
		Role r = new Role();
		BeanUtils.copyProperties(po, r, "users", "authMenus");
		List<AuthMenu> authMenus = new ArrayList<>();
		for (AuthMenuPO authMenuPO : po.getAuthMenus()) {
			AuthMenu authMenu = new AuthMenu();
			BeanUtils.copyProperties(authMenuPO, authMenu, "roles", "children");
			authMenus.add(authMenu);
		}
		r.setAuthMenus(authMenus);
		return r;
	}

	public static Feeds copy(FeedsPO po) {
		Feeds ret = new Feeds();
		BeanUtils.copyProperties(po, ret);
		return ret;
	}

	public static Notify copy(NotifyPO po) {
		Notify ret = new Notify();
		BeanUtils.copyProperties(po, ret);
		return ret;
	}

	public static Favor copy(FavorPO po) {
		Favor ret = new Favor();
		BeanUtils.copyProperties(po, ret);
		return ret;
	}

	public static FriendLink copy(FriendLinkPO po) {
		FriendLink ret = new FriendLink();
		BeanUtils.copyProperties(po, ret);
		return ret;
	}
	
	public static Game copy(GamePO po) {
		Game ret = new Game();
		BeanUtils.copyProperties(po, ret);
		return ret;
	}
}
