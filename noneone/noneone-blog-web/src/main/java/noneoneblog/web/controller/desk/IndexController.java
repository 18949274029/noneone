
package noneoneblog.web.controller.desk;

import javax.servlet.http.HttpServletRequest;

import noneoneblog.base.lang.Consts;
import noneoneblog.web.controller.BaseController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	@RequestMapping(value= {"/", "/index"})
	public String root(ModelMap model, HttpServletRequest request) {
		String order = ServletRequestUtils.getStringParameter(request, "ord", Consts.order.NEWEST);
		model.put("ord", order);
		logger.info("开始查询列表");
		return getView(Views.INDEX);
	}

}
