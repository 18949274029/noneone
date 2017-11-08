package noneoneblog.core.persist.dao.custom;

import noneoneblog.core.data.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by leisure
 */
public interface PostDaoCustom {
    Page<Post> search(Pageable pageable, String q) throws Exception;
    Page<Post> searchByTag(Pageable pageable, String tag);
    void resetIndexs();
}
