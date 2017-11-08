package noneoneblog.core.persist.dao;

import java.util.List;

import noneoneblog.core.persist.entity.AuthMenuPO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuthMenuDao extends JpaRepository<AuthMenuPO, Long>, JpaSpecificationExecutor<AuthMenuPO> {
    List<AuthMenuPO> findAllByParentIdOrderBySortAsc(Long parentId);
    List<AuthMenuPO> findAll();
}
