
package noneoneblog.core.data;

import noneoneblog.core.persist.entity.FeedsPO;

/**
 * 订阅
 * @author leisure
 *
 */
public class Feeds extends FeedsPO {
	private Post post;

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

}
