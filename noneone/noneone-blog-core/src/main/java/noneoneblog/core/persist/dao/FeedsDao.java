
package noneoneblog.core.persist.dao;

import noneoneblog.core.persist.dao.custom.FeedsDaoCustom;
import noneoneblog.core.persist.entity.FeedsPO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author leisure
 *
 */
public interface FeedsDao extends JpaRepository<FeedsPO, Long>, JpaSpecificationExecutor<FeedsPO>, FeedsDaoCustom {
	Page<FeedsPO> findAllByOwnIdOrderByIdDesc(Pageable pageable, long ownId);
	int deleteAllByOwnIdAndAuthorId(long ownId, long authorId);
	void deleteAllByPostId(long postId);
}
