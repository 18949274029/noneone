
package noneoneblog.core.persist.dao.impl;

import java.text.MessageFormat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import noneoneblog.core.data.Feeds;
import noneoneblog.core.persist.dao.custom.FeedsDaoCustom;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author leisure
 *
 */
public class FeedsDaoImpl implements FeedsDaoCustom {
	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	String pattern = "INSERT INTO noneone_feeds (own_id, type, post_id, author_id, created) SELECT user_id, {0}, {1}, {2}, now() FROM noneone_follows WHERE follow_id = {3}";

	@Override
	public int batchAdd(Feeds feeds) {
		String sql = MessageFormat.format(pattern, feeds.getType(), feeds.getPostId(), feeds.getAuthorId(), feeds.getAuthorId());
		return entityManager.createNativeQuery(sql).executeUpdate();
	}

}
