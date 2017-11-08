package noneoneblog.core.data;

import noneoneblog.core.persist.entity.NotifyPO;

/**
 * @author leisure
 */
public class Notify extends NotifyPO {
    // extend
    private User from;
    private Post post;

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
