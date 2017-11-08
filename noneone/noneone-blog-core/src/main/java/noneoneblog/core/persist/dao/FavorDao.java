package noneoneblog.core.persist.dao;

import noneoneblog.core.persist.entity.FavorPO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author leisure
 */
public interface FavorDao extends JpaRepository<FavorPO, Long>, JpaSpecificationExecutor<FavorPO> {
    /**
     * 指定查询
     * @param ownId
     * @param postId
     * @return
     */
    FavorPO findByOwnIdAndPostId(long ownId, long postId);

    Page<FavorPO> findAllByOwnIdOrderByCreatedDesc(Pageable pageable, long ownId);
}
