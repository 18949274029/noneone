package noneoneblog.core.persist.dao;

import noneoneblog.core.persist.entity.GamePO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author leisure
 *
 */
public interface GameDao extends JpaRepository<GamePO, Long>,
		JpaSpecificationExecutor<GamePO> {
	Page<GamePO> findAllByOrderByCreatedDesc(Pageable pageables);
}
