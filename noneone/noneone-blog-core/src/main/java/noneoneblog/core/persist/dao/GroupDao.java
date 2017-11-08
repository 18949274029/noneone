
package noneoneblog.core.persist.dao;

import java.util.List;

import noneoneblog.core.persist.entity.GroupPO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author leisure
 *
 */
public interface GroupDao extends JpaRepository<GroupPO, Integer>, JpaSpecificationExecutor<GroupPO> {
	List<GroupPO> findAllByStatus(int status);
	GroupPO findByKey(String key);
}
