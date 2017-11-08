
package noneoneblog.core.persist.dao;

import java.util.List;
import java.util.Set;

import noneoneblog.core.persist.entity.AttachPO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author leisure
 *
 */
public interface AttachDao extends JpaRepository<AttachPO, Long>, JpaSpecificationExecutor<AttachPO> {
	List<AttachPO> findAllByToId(long toId);
	List<AttachPO> findAllByToIdIn(Set<Long> toIds);
	List<AttachPO> findAllByIdIn(Set<Long> ids);
	void deleteAllByToId(long toId);
}
