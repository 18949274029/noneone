
package noneoneblog.web.controller.desk;

import noneoneblog.web.controller.BaseController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author leisure
 *
 */
@Controller
public class AboutController extends BaseController {
	
	@RequestMapping("/about")
	public String about() {
		return getView("/about/about");
	}
	
	@RequestMapping("/joinus")
	public String joinus() {
		return getView("/about/joinus");
	}
	
	@RequestMapping("/faqs")
	public String faqs() {
		return getView("/about/faqs");
	}

}
