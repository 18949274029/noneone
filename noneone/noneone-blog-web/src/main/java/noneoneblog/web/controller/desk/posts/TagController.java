
package noneoneblog.web.controller.desk.posts;

import noneoneblog.core.data.Post;
import noneoneblog.core.persist.service.PostService;
import noneoneblog.web.controller.BaseController;
import noneoneblog.web.controller.desk.Views;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 标签
 * @author leisure
 *
 */
@Controller
public class TagController extends BaseController {
    @Autowired
    private PostService postService;

    @RequestMapping(value="/tag/{tag}")
    public String tag(@PathVariable String tag, ModelMap model) {
        Pageable pageable = wrapPageable();
        try {
            if (StringUtils.isNotEmpty(tag)) {
                Page<Post> page = postService.searchByTag(pageable, tag);
                model.put("page", page);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.put("tag", tag);
        return getView(Views.TAGS_TAG);
    }

}
