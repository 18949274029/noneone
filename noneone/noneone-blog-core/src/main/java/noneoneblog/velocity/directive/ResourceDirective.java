
package noneoneblog.velocity.directive;

import java.io.IOException;

import noneoneblog.base.context.Global;
import noneoneblog.velocity.BaseDirective;
import noneoneblog.velocity.handler.RenderHandler;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * 资源路径处理
 * - 当 ${resource.domain} = true 时，自己在资源地址前面追加host
 * @author leisure
 *
 */
public class ResourceDirective extends BaseDirective {
    @Override
    public String getName() {
        return "resource";
    }

    @Override
    public int getType() {
        return LINE;
    }

	@Override
	public void initBean() {
	}

	@Override
	public boolean render(RenderHandler handler)
			throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException {
		String src = handler.getStringParameter(0);
        String base = handler.getRequest().getContextPath();
        
        // 判断是否启用图片域名
        if (Global.getImageDomain()) {
        	base = Global.getImageHost();
        }
        
        StringBuffer buf = new StringBuffer();
        
        buf.append(base);
        buf.append(src);
        handler.write(buf.toString());
        return true;
	}
}
