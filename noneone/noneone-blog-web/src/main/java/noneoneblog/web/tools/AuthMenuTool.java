package noneoneblog.web.tools;

import java.util.List;

import noneoneblog.core.data.AuthMenu;
import noneoneblog.core.persist.service.AuthMenuService;

import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.config.ValidScope;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by leisure
 */
@DefaultKey("auth")
@ValidScope(Scope.APPLICATION)
public class AuthMenuTool {

    public List<AuthMenu> findByParentId(long pid) {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();

        AuthMenuService authMenuService = (AuthMenuService) wac.getBean(AuthMenuService.class);
        List<AuthMenu> list = authMenuService.findByParentId(pid);
        return list;
    }

}
