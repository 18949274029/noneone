
package noneoneblog.core.persist.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 动态
 * 
 * @author leisure
 * 
 */
@Entity
@Table(name = "mto_feeds")
public class FeedsPO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "own_id")
	private long ownId; // 所属用户Id

	private int type;

	@Column(name = "post_id")
	private long postId; // 目标Id

	@Column(name = "author_id")
	private long authorId; // 作者
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOwnId() {
		return ownId;
	}

	public void setOwnId(long ownId) {
		this.ownId = ownId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}

	public long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(long authorId) {
		this.authorId = authorId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
