package noneoneblog.web.controller.api;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import noneoneblog.base.utils.AESUtil;
import noneoneblog.core.biz.PostBiz;
import noneoneblog.core.data.Post;
import noneoneblog.web.controller.BaseController;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;

/**
 * 网络爬虫
 * @author leisure
 *
 */
@Controller
@RequestMapping("/spider")
public class SpiderController extends BaseController{
	@Autowired
	private PostBiz postBiz;
	/**
	 * 爬虫发布
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/spiderSubmit", method = RequestMethod.POST)
	public String spiderPost(String post, HttpServletRequest request) {
		String text = null;
		try {
			text = new String(AESUtil.decrypt(post.getBytes(), "lifeifei168168"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			return "error";
		}
		if (text != null && StringUtils.isNotBlank(text)) {
			Post p = JSONObject.parseObject(text,Post.class);
			extractImages(p);
			postBiz.post(p);
			return "ok";
		}
		return "error";
	}
}
