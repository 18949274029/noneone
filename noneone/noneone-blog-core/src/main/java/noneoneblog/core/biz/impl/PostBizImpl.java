
package noneoneblog.core.biz.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import noneoneblog.base.upload.FileRepo;
import noneoneblog.core.biz.PostBiz;
import noneoneblog.core.data.Attach;
import noneoneblog.core.data.Post;
import noneoneblog.core.event.FeedsEvent;
import noneoneblog.core.persist.service.AttachService;
import noneoneblog.core.persist.service.FeedsService;
import noneoneblog.core.persist.service.PostService;
import noneoneblog.core.persist.utils.Http4ClientUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author leisure
 * 
 */
@Service
public class PostBizImpl implements PostBiz {
	@Autowired
	private PostService postService;
	@Autowired
	private AttachService attachService;
	@Autowired
	private FeedsService feedsService;
	@Autowired
	private FileRepo fileRepo;
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * 分页查询文章, 带缓存
	 * - 缓存key规则: list_分组ID排序方式_页码_每页条数
	 * @param pageable
	 * @param group
	 * @param ord
	 * @return
	 */
	@Override
	@Cacheable(value = "postsCaches", key = "'list_' + #group + #ord + '_' + #pageable.getPageNumber() + '_' + #pageable.getPageSize()")
	public Page<Post> paging(Pageable pageable, int group, String ord) {
		return postService.paging(pageable, group, ord, true);
	}
	
	@Override
	@Cacheable(value = "postsCaches", key = "'uhome' + #uid + '_' + #pageable.getPageNumber()")
	public Page<Post> pagingByAuthorId(Pageable pageable, long uid) {
		return postService.pagingByAuthorId(pageable, uid);
	}

	@Override
	@Cacheable(value = "postsCaches", key = "'view_' + #id")
	public Post getPost(long id) {
		return postService.get(id);
	}

	@Override
	@CacheEvict(value = "postsCaches", allEntries = true)
	public void post(Post post) {
		long id = postService.post(post);

		FeedsEvent event = new FeedsEvent("feedsEvent");
		event.setPostId(id);
		event.setAuthorId(post.getAuthorId());
		applicationContext.publishEvent(event);
	}

	@Override
	@CacheEvict(value = "postsCaches", allEntries = true)
	public void updateFeatured(long id, int featured) {
		postService.updateFeatured(id, featured);
	}

	@Override
	@Cacheable(value = "postsCaches", key = "'post_recents'")
	public List<Post> findRecents(int maxResults, long ignoreUserId) {
		return postService.findLatests(maxResults, ignoreUserId);
	}

	@Override
	@Cacheable(value = "postsCaches", key = "'post_hots'")
	public List<Post> findHots(int maxResults, long ignoreUserId) {
		return postService.findHots(maxResults, ignoreUserId);
	}
	
	@Override
	@CacheEvict(value = "postsCaches", allEntries = true)
	public void delete(long id, long authorId) {
		List<Attach> atts = attachService.findByTarget(id);
		postService.delete(id, authorId);

		if (!atts.isEmpty()) {
			atts.forEach(a -> {
				fileRepo.deleteFile(a.getPreview());
				fileRepo.deleteFile(a.getOriginal());
			});
		}

		feedsService.deleteByTarget(id);
	}

	@Override
	@CacheEvict(value = "postsCaches", allEntries = true)
	public void delete(Collection<Long> ids) {
		for (Long id : ids) {
			List<Attach> atts = attachService.findByTarget(id);
			postService.delete(id);

			// 时刻保持清洁, 物理删除图片
			if (!atts.isEmpty()) {
				atts.forEach(a -> {
					fileRepo.deleteFile(a.getPreview());
					fileRepo.deleteFile(a.getOriginal());
				});
			}

			feedsService.deleteByTarget(id);
		}
	}

	@Override
	@CacheEvict(value = "postsCaches", key = "'view_' + #postId")
	public void favor(long userId, long postId) {
		postService.favor(userId, postId);
	}

	@Override
	@CacheEvict(value = "postsCaches", key = "'view_' + #postId")
	public void unfavor(long userId, long postId) {
		postService.unfavor(userId, postId);
	}

	/**
	 * 更新文章：更新文章并清空缓存
	 * @param p
	 */
	@Override
	@CacheEvict(value = "postsCaches", allEntries = true)
	public void update(Post p) {
		postService.update(p);
	}

	@Override
	public Post findPost(String title) {
		return postService.findPost(title);
	}

	@Override
	public String pushBaidu(BigInteger id) {
		String result = null;
		try{
		List<BigInteger> ids = postService.getIDsRTId(id);
		if (ids!=null&&ids.size()>0) {
			String parames = "";
			for (int i = 0; i < ids.size(); i++) {
				parames += "https://www.noneone.cn/view/"+ids.get(i)+"\n";
			}
			String url = "http://data.zz.baidu.com/urls?site=https://www.noneone.cn&token=K2mOB2ps0dPa1wVj";
			result = Http4ClientUtil.postPlain(url, parames);
		}else{
			result="查无推送数据!";
		}
		}catch(Exception e){
			result="推送失败!";
		}
		return result;
	}

}
