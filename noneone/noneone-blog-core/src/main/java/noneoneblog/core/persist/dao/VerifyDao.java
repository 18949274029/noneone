
package noneoneblog.core.persist.dao;

import noneoneblog.core.persist.entity.VerifyPO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author leisure
 */
public interface VerifyDao extends JpaRepository<VerifyPO, Long>, JpaSpecificationExecutor<VerifyPO> {
    VerifyPO findByUserIdAndType(long userId, int type);
    VerifyPO findByUserId(long userId);
}
