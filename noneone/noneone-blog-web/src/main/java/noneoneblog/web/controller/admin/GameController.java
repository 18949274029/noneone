package noneoneblog.web.controller.admin;

import javax.servlet.http.HttpServletRequest;

import noneoneblog.core.data.Game;
import noneoneblog.core.persist.service.GameService;
import noneoneblog.web.controller.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author leisure
 *
 */
@Controller
@RequestMapping(value = "game")
public class GameController extends BaseController {
	@Autowired
	private GameService gameService;

	@RequestMapping(value = "/list")
	public String list(ModelMap model, HttpServletRequest request) {
		Pageable pageable = wrapPageable();
		Page<Game> page = gameService.getGameLists(pageable);
		model.put("page", page);
		return "/game/list";
	}


	@RequestMapping(value = "/open")
	public String open(ModelMap model, HttpServletRequest request,String url) {
		return url;
	}
}
