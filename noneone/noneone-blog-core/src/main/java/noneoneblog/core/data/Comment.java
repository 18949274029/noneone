package noneoneblog.core.data;

import java.io.Serializable;
import java.util.Date;

import noneoneblog.core.persist.entity.CommentPO;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author leisure
 * 
 */
public class Comment extends CommentPO implements Serializable {
	private static final long serialVersionUID = 9192186139010913437L;

	// extend parameter
	private User author;
	private Comment parent;
	private Post post;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	public Date getCreated() {
		return super.getCreated();
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Comment getParent() {
		return parent;
	}

	public void setParent(Comment parent) {
		this.parent = parent;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}
}
