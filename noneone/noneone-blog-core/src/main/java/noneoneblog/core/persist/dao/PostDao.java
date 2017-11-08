
package noneoneblog.core.persist.dao;

import java.util.Collection;
import java.util.List;

import noneoneblog.core.persist.dao.custom.PostDaoCustom;
import noneoneblog.core.persist.entity.PostPO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author leisure
 *
 */
public interface PostDao extends JpaRepository<PostPO, Long>, JpaSpecificationExecutor<PostPO>, PostDaoCustom {
	/**
	 * 查询指定用户
	 * @param pageable
	 * @param authorId
	 * @return
	 */
	Page<PostPO> findAllByAuthorIdOrderByCreatedDesc(Pageable pageable, long authorId);

	// findLatests
	List<PostPO> findTop12ByOrderByCreatedDesc();

	// findHots
	List<PostPO> findTop12ByOrderByViewsDesc();

	List<PostPO> findAllByIdIn(Collection<Long> id);

	@Query("select coalesce(max(p.featured), 0) from PostPO p")
	int maxFeatured();
	
}
