
package noneoneblog.web.controller.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import noneoneblog.base.data.Data;
import noneoneblog.base.lang.Consts;
import noneoneblog.core.data.Config;
import noneoneblog.core.persist.service.ConfigService;
import noneoneblog.core.persist.service.GroupService;
import noneoneblog.core.persist.service.PostService;
import noneoneblog.web.controller.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 系统配置
 *
 * @author leisure
 *
 */
@Controller
@RequestMapping("/admin/config")
public class ConfigsController extends BaseController {
	@Autowired
	private EhCacheCacheManager ehcacheManager;
	@Autowired
	private ConfigService configService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private ServletContext servletContext;
	@Autowired
	private PostService postService;

	@RequestMapping("/")
	public String list(ModelMap model) {
		Collection<String> cacheNames = ehcacheManager.getCacheNames();

		model.put("cacheNames", cacheNames);
		model.put("configs", configService.findAll2Map());
		return "/admin/configs/main";
	}
	
	@RequestMapping("/update")
	@SuppressWarnings("unchecked")
	public String update(HttpServletRequest request, ModelMap model) {
		Map<String, String[]> params = request.getParameterMap();

		List<Config> configs = new ArrayList<>();

		params.forEach((k, v) -> {
			Config conf = new Config();
			conf.setKey(k);
			conf.setValue(v[0]);

			configs.add(conf);
		});
		configService.update(configs);
		return "redirect:/admin/config/";
	}
	
	@RequestMapping("/flush_cache")
	public @ResponseBody
	Data flushCache() {
		ehcacheManager.getCacheManager().clearAll();
		return Data.success("操作成功", Data.NOOP);
	}

	@RequestMapping("/flush_conf")
	public @ResponseBody Data flushFiledia() {
		// 刷新系统变量
		List<Config> configs = configService.findAll();

		Map<String, String> configMap = new HashMap<>();
		configs.forEach(conf -> {
			servletContext.setAttribute(conf.getKey(), conf.getValue());
			configMap.put(conf.getKey(), conf.getValue());
		});

		appContext.setConfig(configMap);

		// 刷新文章Group
		servletContext.setAttribute("groups", groupService.findAll(Consts.STATUS_NORMAL));
		return Data.success("操作成功", Data.NOOP);
	}

	@RequestMapping("/flush_indexs")
	public @ResponseBody Data flushIndexs() {
		postService.resetIndexs();
		return Data.success("操作成功", Data.NOOP);
	}
	
}
