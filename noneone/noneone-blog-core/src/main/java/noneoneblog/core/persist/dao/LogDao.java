
package noneoneblog.core.persist.dao;

import noneoneblog.core.persist.entity.LogPO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author leisure
 *
 */
public interface LogDao extends JpaRepository<LogPO, Long>, JpaSpecificationExecutor<LogPO> {
}
