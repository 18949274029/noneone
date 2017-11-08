package noneoneblog.core.persist.dao;

import java.util.List;

import noneoneblog.core.persist.entity.FriendLinkPO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author leisure
 */
public interface FriendLinkDao extends JpaRepository<FriendLinkPO, Long>, JpaSpecificationExecutor<FriendLinkPO> {
    List<FriendLinkPO> findAllByOrderBySortDesc();
}
