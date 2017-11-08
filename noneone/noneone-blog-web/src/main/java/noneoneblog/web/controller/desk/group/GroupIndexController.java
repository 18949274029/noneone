
package noneoneblog.web.controller.desk.group;

import javax.servlet.http.HttpServletRequest;

import noneoneblog.base.lang.Consts;
import noneoneblog.core.data.Group;
import noneoneblog.core.persist.service.GroupService;
import noneoneblog.web.controller.BaseController;
import noneoneblog.web.controller.desk.Views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Group 主页
 * @author leisure
 *
 */
@Controller
public class GroupIndexController extends BaseController {
	@Autowired
	private GroupService groupService;
	
	@RequestMapping("/g/{groupKey}")
	public String root(@PathVariable String groupKey, ModelMap model,
			HttpServletRequest request) {
		// init params
		String order = ServletRequestUtils.getStringParameter(request, "ord", Consts.order.NEWEST);
		Group group = groupService.getByKey(groupKey);
		
		// callback params
		model.put("group", group);
		model.put("ord", order);
		return getView(Views.ROUTE_POST_INDEX);
	}
	
}
