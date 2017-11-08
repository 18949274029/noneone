
package noneoneblog.web.controller.desk.account;

import noneoneblog.base.data.Data;
import noneoneblog.core.data.AccountProfile;
import noneoneblog.core.persist.service.UserService;
import noneoneblog.web.controller.BaseController;
import noneoneblog.web.controller.desk.Views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author leisure
 *
 */
@Controller
@RequestMapping("/account")
public class PasswordController extends BaseController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/password", method = RequestMethod.GET)
	public String view() {
		return getView(Views.ACCOUNT_PASSWORD);
	}
	
	@RequestMapping(value = "/password", method = RequestMethod.POST)
	public String post(String oldPassword, String password, ModelMap model) {
		Data data;
		try {
			AccountProfile profile = getSubject().getProfile();
			userService.updatePassword(profile.getId(), oldPassword, password);
			
			data = Data.success("操作成功", Data.NOOP);
		} catch (Exception e) {
			data = Data.failure(e.getMessage());
		}
		model.put("data", data);
		return getView(Views.ACCOUNT_PASSWORD);
	}

}
