package noneoneblog.core.biz;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import noneoneblog.core.data.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 文章业务层, 带缓存策略
 *
 * - 如果不使用缓存, 则直接调用Service
 * 
 * @author leisure
 *
 */
public interface PostBiz {
	/**
	 * 分页查询文章
	 * 
	 * @param pageable
	 * @param group
	 * @param ord
	 * @return
	 */
	Page<Post> paging(Pageable pageable, int group, String ord);
	
	/**
	 * 查询指定用户的文章
	 * 
	 * @param pageable
	 * @param uid
	 * @return
	 */
	Page<Post> pagingByAuthorId(Pageable pageable, long uid);
	
	/**
	 * 从缓存中获取文章
	 * 
	 * @param id
	 * @return
	 */
	Post getPost(long id);
	
	/**
	 * 发布文章, 并清除缓存
	 * 
	 * @param post
	 */
	long post(Post post);

	/**
	 * 修改文章推荐状态
	 * @param id
	 * @param featured
	 */
	void updateFeatured(long id, int featured);

	/**
	 * 查询最近更新的文章
	 *
	 * @param maxResults
	 * @param ignoreUserId
	 * @return
	 */
	List<Post> findRecents(int maxResults, long ignoreUserId);

	/**
	 * 查询热门文章
	 *
	 * @param maxResults
	 * @param ignoreUserId
	 * @return
	 */
	List<Post> findHots(int maxResults, long ignoreUserId);

	/**
	 * 删除文章, 且刷新缓存
	 *
	 * @param id
	 * @param authorId
	 */
	void delete(long id, long authorId);

	/**
	 * 批量删除文章, 且刷新缓存
	 *
	 * @param ids
	 */
	void delete(Collection<Long> ids);

	/**
	 * 喜欢文章
	 * @param userId
	 * @param postId
	 */
	void favor(long userId, long postId);

	/**
	 * 取消喜欢文章
	 * @param userId
	 * @param postId
	 */
	void unfavor(long userId, long postId);

	/**
	 * 更新文章：更新文章并清空缓存
	 * @param p
	 */
	void update(Post p);
	/**
	 * 根据标题查询文章
	 */
	Post findPost(String title);
	
	String pushBaidu(Long start,Long end,String type);
	
}
