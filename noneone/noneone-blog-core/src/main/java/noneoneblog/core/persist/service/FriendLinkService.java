package noneoneblog.core.persist.service;

import java.util.List;

import noneoneblog.core.data.FriendLink;

/**
 * @author leisure
 */
public interface FriendLinkService {

    void save(FriendLink friendLink);

    void delete(long id);

    FriendLink findById(long id);

    List<FriendLink> findAll();
}
