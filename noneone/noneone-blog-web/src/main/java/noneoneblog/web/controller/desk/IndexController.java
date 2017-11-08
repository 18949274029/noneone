
package noneoneblog.web.controller.desk;

import javax.servlet.http.HttpServletRequest;

import noneoneblog.base.lang.Consts;
import noneoneblog.web.controller.BaseController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author leisure
 *
 */
@Controller
public class IndexController extends BaseController{
	
	@RequestMapping(value= {"/", "/index"})
	public String root(ModelMap model, HttpServletRequest request) {
		String order = ServletRequestUtils.getStringParameter(request, "ord", Consts.order.NEWEST);
		model.put("ord", order);
		return getView(Views.INDEX);
	}

}
