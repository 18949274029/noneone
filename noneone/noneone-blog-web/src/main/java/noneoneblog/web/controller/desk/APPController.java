package noneoneblog.web.controller.desk;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

import noneoneblog.web.controller.BaseController;
/**
 * @author leisure
 */

@Controller
@RequestMapping("/app")
public class APPController extends BaseController{

	@RequestMapping("/appInfo")
	public String about() {
		return getView("/app/app");
	}
}
