package noneoneblog.core.data;

import noneoneblog.core.persist.entity.FavorPO;

/**
 * @author leisure
 */
public class Favor extends FavorPO {
    // extend
    private Post post;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
