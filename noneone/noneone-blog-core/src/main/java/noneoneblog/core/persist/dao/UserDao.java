
package noneoneblog.core.persist.dao;

import java.util.List;
import java.util.Set;

import noneoneblog.core.persist.entity.UserPO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author leisure
 */
public interface UserDao extends JpaRepository<UserPO, Long>, JpaSpecificationExecutor<UserPO> {
    UserPO findByUsername(String username);

    UserPO findByEmail(String email);
    
    List<UserPO> findTop8ByOrderByFansDesc();

    Page<UserPO> findAllByOrderByIdDesc(Pageable pageable);

    List<UserPO> findAllByIdIn(Set<Long> ids);

}
