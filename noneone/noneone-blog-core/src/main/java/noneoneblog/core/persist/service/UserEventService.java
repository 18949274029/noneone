
package noneoneblog.core.persist.service;

/**
 * @author leisure
 */
public interface UserEventService {
    /**
     * 自增发布文章数
     * @param userId
     * @param postId
     */
    void identityPost(Long userId, long postId, boolean identity);

    /**
     * 自增评论数
     * @param userId
     * @param commentId
     */
    void identityComment(Long userId, long commentId, boolean identity);

    /**
     * 自增关注数
     * @param userId
     * @param followId
     */
    void identityFollow(Long userId, long followId, boolean identity);

    /**
     * 自增粉丝数
     * @param userId
     * @param fansId
     */
    void identityFans(Long userId, long fansId, boolean identity);

    /**
     * 自增收藏数
     * @param userId
     * @param targetType
     * @param targetId
     */
    void identityFavors(Long userId, boolean identity, int targetType, long targetId);
}
