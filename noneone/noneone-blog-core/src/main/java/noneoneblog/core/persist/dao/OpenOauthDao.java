
package noneoneblog.core.persist.dao;

import noneoneblog.core.persist.entity.OpenOauthPO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 第三方开发授权登录
 * @author leisure
 */
public interface OpenOauthDao extends JpaRepository<OpenOauthPO, Long>, JpaSpecificationExecutor<OpenOauthPO> {
    OpenOauthPO findByAccessToken(String accessToken);

    OpenOauthPO findByOauthUserId(String oauthUserId);
    
    OpenOauthPO findByUserId(long userId);
}
