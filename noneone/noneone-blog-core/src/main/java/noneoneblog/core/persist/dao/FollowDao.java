
package noneoneblog.core.persist.dao;

import java.util.List;

import noneoneblog.core.persist.entity.FollowPO;
import noneoneblog.core.persist.entity.UserPO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author leisure
 * 
 */
public interface FollowDao extends JpaRepository<FollowPO, Long>, JpaSpecificationExecutor<FollowPO> {

	/**
	 * 检查是否已关注
	 * 
	 * @param user
	 * @param follow
	 * @return
	 */
	FollowPO findByUserAndFollow(UserPO user, UserPO follow);

	/**
	 * 查询我的关注
	 * 
	 * @param pageable
	 * @param user
	 */
	Page<FollowPO> findAllByUser(Pageable pageable, UserPO user);

	/**
	 * 查询关注我的 (我的粉丝)
	 * 
	 * @param pageable
	 * @param follow
	 */
	Page<FollowPO> findAllByFollow(Pageable pageable, UserPO follow);

	/**
	 * 检查是否相互关注
	 * 
	 * @param userId
	 * @param followId
	 * @return
	 */
	@Query("from FollowPO f1 where f1.user.id = :userId and f1.follow.id = :followId and f1.user.id in (select f2.follow.id from FollowPO f2 where f2.user.id = :followId)")
	List<FollowPO> findAllCrossFollow(@Param("userId") long userId, @Param("followId") long followId);
	
	/**
	 * 取消关注
	 * 
	 * @param user
	 * @param follow
	 * @return
	 */
	int deleteByUserAndFollow(UserPO user, UserPO follow);
}
