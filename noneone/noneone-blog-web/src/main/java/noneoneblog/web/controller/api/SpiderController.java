package noneoneblog.web.controller.api;

import com.alibaba.fastjson.JSONObject;
import noneoneblog.base.utils.AESUtil;
import noneoneblog.core.biz.PostBiz;
import noneoneblog.core.data.Post;
import noneoneblog.web.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 网络爬虫
 *
 * @author leisure
 */
@Controller
@RequestMapping("/spider")
public class SpiderController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(SpiderController.class);
    @Autowired
    private PostBiz postBiz;

    /**
     * 爬虫发布
     *
     * @param
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
                Post p = JSONObject.parseObject(text, Post.class);
                if (p == null || p.getTitle() == null || p.getContent() == null) {
                    return "error";
                }
                //先检测有没有重复标题的
                Post select_post = postBiz.findPost(p.getTitle());
                if (select_post != null) {
                    return "repeat";
                }
                extractImages(p);
                long id = postBiz.post(p);
                return "ok," + id;
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
    @RequestMapping(value = "/pushBaidu")
    @ResponseBody
    public String pushBaidu(String start, String end, String type, HttpServletRequest request) {
        if (StringUtils.isEmpty(start) || StringUtils.isEmpty(end) || StringUtils.isEmpty(type)) {
            return "parame is null";
        }
        return postBiz.pushBaidu(new Long(start), new Long(end), Integer.parseInt(type));
    }

    @RequestMapping(value = "/getIp")
    @ResponseBody
    public String getIp(HttpServletRequest request) {
        String ip = getIpAddress(request);
        logger.info("入口IP ==>"+ip);
        return ip;
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 参考文章： http://developer.51cto.com/art/201111/305181.htm
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
