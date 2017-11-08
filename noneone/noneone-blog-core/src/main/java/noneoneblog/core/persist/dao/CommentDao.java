
package noneoneblog.core.persist.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import noneoneblog.core.persist.entity.CommentPO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author leisure
 *
 */
public interface CommentDao extends JpaRepository<CommentPO, Long>, JpaSpecificationExecutor<CommentPO> {
	Page<CommentPO> findAll(Pageable pageable);
	Page<CommentPO> findAllByToIdOrderByCreatedDesc(Pageable pageable, long toId);
	Page<CommentPO> findAllByAuthorIdOrderByCreatedDesc(Pageable pageable, long authorId);
	List<CommentPO> findByIdIn(Set<Long> ids);
	List<CommentPO> findAllByAuthorIdAndToIdOrderByCreatedDesc(long authorId, long toId);

	int deleteAllByIdIn(Collection<Long> ids);
}
