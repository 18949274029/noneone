
package noneoneblog.core.persist.service;

import noneoneblog.core.data.OpenOauth;
import noneoneblog.core.data.User;

/**
 * @author leisure
 */
public interface OpenOauthService {
    //通过 oauth_token 查询 user
    User getUserByOauthToken(String oauth_token);

    OpenOauth getOauthByToken(String oauth_token);
    
    OpenOauth getOauthByOauthUserId(String oauthUserId);

    OpenOauth getOauthByUid(long userId);

    boolean checkIsOriginalPassword(long userId);

    void saveOauthToken(OpenOauth oauth);

}
