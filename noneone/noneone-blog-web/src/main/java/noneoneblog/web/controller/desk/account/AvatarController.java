
package noneoneblog.web.controller.desk.account;

import java.io.File;

import noneoneblog.base.context.AppContext;
import noneoneblog.base.context.Global;
import noneoneblog.base.data.Data;
import noneoneblog.base.lang.Consts;
import noneoneblog.base.utils.FilePathUtils;
import noneoneblog.base.utils.ImageUtils;
import noneoneblog.core.data.AccountProfile;
import noneoneblog.core.persist.service.UserService;
import noneoneblog.web.controller.BaseController;
import noneoneblog.web.controller.desk.Views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author leisure
 *
 */
@Controller
@RequestMapping("/account")
public class AvatarController extends BaseController {
	@Autowired
	private AppContext appContext;
	@Autowired
	private UserService userService;
    
	@RequestMapping(value = "/avatar", method = RequestMethod.GET)
	public String view() {
		return getView(Views.ACCOUNT_AVATAR);
	}
	
	@RequestMapping(value = "/avatar", method = RequestMethod.POST)
	public String post(String path, Float x, Float y, Float width, Float height, ModelMap model) {
		AccountProfile profile = getSubject().getProfile();
		
		if (StringUtils.isEmpty(path)) {
			model.put("data", Data.failure("请选择图片"));
			return getView(Views.ACCOUNT_AVATAR);
		}
		////判断存储模式
		if (Consts.ABSOLUTE.equals(Global.getConfig("store.repo"))) {
			path.substring(Global.getImageHost().length());
		}
		
		
		if (width != null && height != null) {
			String root = fileRepoFactory.select().getRoot();
			File temp = new File(root + path);
			File scale = null;
			
			// 目标目录
			String ava100 = appContext.getAvaDir() + getAvaPath(profile.getId(), 100);
			String dest = root + ava100;
			try {
				// 判断父目录是否存在
				File f = new File(dest);
		        if(!f.getParentFile().exists()){
		            f.getParentFile().mkdirs();
		        }
		        // 在目标目录下生成截图
		        String scalePath = f.getParent() + "/" + profile.getId() + ".jpg";
				ImageUtils.truncateImage(temp.getAbsolutePath(), scalePath, x.intValue(), y.intValue(), width.intValue());
		        
				// 对结果图片进行压缩
				ImageUtils.scaleImage(scalePath, dest, 100);

				AccountProfile user = userService.updateAvatar(profile.getId(), ava100);
				putProfile(user);
				
				scale = new File(scalePath);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				temp.delete();
				if (scale != null) {
					scale.delete();
				}
			}
		}
		return "redirect:/account/profile";
	}
	
	public String getAvaPath(long uid, int size) {
		String base = FilePathUtils.getAvatar(uid);
		return String.format("/%s_%d.jpg", base, size);
	}
}
