
package noneoneblog.velocity.directive;

import java.io.IOException;

import javax.servlet.ServletRequest;

import noneoneblog.base.context.SpringContextHolder;
import noneoneblog.base.lang.Consts;
import noneoneblog.core.biz.PostBiz;
import noneoneblog.core.data.Post;
import noneoneblog.velocity.BaseDirective;
import noneoneblog.velocity.handler.RenderHandler;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.ServletRequestUtils;

/**
 * 文章内容查询
 * 
 * 示例：
 * 	请求：http://noneone.cn/index?ord=newest&pn=2
 *  使用：#contents(groupId, 'paging')
 *  解析： ord, pn 等参数从request中获取 , groupId 从标签中获取
 *  
 * @author leisure
 *
 */
public class ContentsDirective extends BaseDirective {
	private PostBiz postPlanet;

	@Override
	public void initBean() {
		postPlanet = SpringContextHolder.getBean(PostBiz.class);
	}

	@Override
	public String getName() {
		return "contents";
	}

	@Override
	public int getType() {
		return BLOCK;
	}

	@Override
	public boolean render(RenderHandler handler) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException {
		ServletRequest request = handler.getRequest();
		
		// request 获取参数
        String ord = ServletRequestUtils.getStringParameter(request, "ord", Consts.order.NEWEST);
        int pn = ServletRequestUtils.getIntParameter(request, "pn", 1);
        
        // 标签中获取参数
        int groupId = handler.getIntParameter(0);
        String alias = handler.getStringParameter(1);

		Pageable pageable = new PageRequest(pn - 1, 10);
		Page<Post> result = postPlanet.paging(pageable, groupId, ord);

		handler.put(alias, result);
		handler.doRender();
		
		postRender(handler.getContext());
		return true;
	}

}
