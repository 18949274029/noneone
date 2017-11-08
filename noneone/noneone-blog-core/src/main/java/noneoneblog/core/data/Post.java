
package noneoneblog.core.data;

import java.io.Serializable;
import java.util.List;

import noneoneblog.base.lang.Consts;
import noneoneblog.core.persist.entity.PostAttribute;
import noneoneblog.core.persist.entity.PostPO;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author leisure
 * 
 */
public class Post extends PostPO implements Serializable {
	private static final long serialVersionUID = -1144627551517707139L;

	private String content;

	// extends
	private List<Attach> albums;
	private Attach album;
	private User author;
	
	@JsonIgnore
	private PostAttribute attribute;
	
	public String[] getTagsArray() {
		if (StringUtils.isNotBlank(super.getTags())) {
			return super.getTags().split(Consts.SEPARATOR);
		}
		return null;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public List<Attach> getAlbums() {
		return albums;
	}

	public void setAlbums(List<Attach> albums) {
		this.albums = albums;
	}

	public Attach getAlbum() {
		return album;
	}

	public void setAlbum(Attach album) {
		this.album = album;
	}

	public PostAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(PostAttribute attribute) {
		this.attribute = attribute;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
