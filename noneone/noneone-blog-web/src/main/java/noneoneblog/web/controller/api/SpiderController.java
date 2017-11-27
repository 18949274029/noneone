package noneoneblog.web.controller.api;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

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
import org.springframework.web.bind.annotation.ResponseBody;

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
	 @ResponseBody
	public String spiderPost(String post, HttpServletRequest request) {
		String text = null;
		try {
			 AESUtil aes = new AESUtil("lifeifei11111111");
			 text = aes.Decrypt(post);
			if (text != null && StringUtils.isNotBlank(text)) {
				Post p = JSONObject.parseObject(text,Post.class);
				if (p==null||p.getTitle()==null||p.getContent()==null) {
					return "error";
				}
				//先检测有没有重复标题的
			  Post select_post = postBiz.findPost(p.getTitle());
				if (select_post!=null) {
					return "repeat";
				}
				extractImages(p);
				postBiz.post(p);
				return "ok";
			}
		} catch (Exception e) {
			return "error";
		}
		return "error";
	}
	
	/**
	 * 百度推送
	 * id 文章列表大于id的
	 */
	@RequestMapping(value = "/pushBaidu", method = RequestMethod.POST)
	 @ResponseBody
	public String pushBaidu(String id, HttpServletRequest request) {
		if (StringUtils.isEmpty(id)) {
			return "id is null";
		}
		String result = postBiz.pushBaidu(new BigInteger(id));
		return result;
	}
}
