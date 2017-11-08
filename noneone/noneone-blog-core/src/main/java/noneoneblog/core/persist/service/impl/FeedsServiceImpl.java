
package noneoneblog.core.persist.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import noneoneblog.core.data.Feeds;
import noneoneblog.core.data.Post;
import noneoneblog.core.persist.dao.FeedsDao;
import noneoneblog.core.persist.entity.FeedsPO;
import noneoneblog.core.persist.service.FeedsService;
import noneoneblog.core.persist.service.PostService;
import noneoneblog.core.persist.utils.BeanMapUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author leisure
 *
 */
@Service
public class FeedsServiceImpl implements FeedsService {
	@Autowired
	private FeedsDao feedsDao;
	@Autowired
	private PostService postService;

	@Override
	@Transactional
	public int add(Feeds feeds) {
		FeedsPO po = new FeedsPO();
		BeanUtils.copyProperties(feeds, po);

		po.setCreated(new Date());

		// 给自己保存一条
		feedsDao.save(po);

		// 派发给粉丝
		return feedsDao.batchAdd(feeds);
	}

	@Override
	@Transactional
	public int deleteByAuthorId(long ownId, long authorId) {
		return feedsDao.deleteAllByOwnIdAndAuthorId(ownId, authorId);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Feeds> findUserFeeds(Pageable pageable, long ownId) {
		Page<FeedsPO> page = feedsDao.findAllByOwnIdOrderByIdDesc(pageable, ownId);

		List<Feeds> rets = new ArrayList<>();

		Set<Long> postIds = new HashSet<>();

		for (FeedsPO po : page.getContent()) {
			Feeds f = BeanMapUtils.copy(po);
			rets.add(f);

			postIds.add(f.getPostId());
		}

		// 加载文章
		Map<Long, Post> postMap = postService.findMultileMapByIds(postIds);

		for (Feeds f : rets) {
			f.setPost(postMap.get(f.getPostId()));
		}
		return new PageImpl<>(rets, pageable, page.getTotalElements());
	}

	@Override
	@Transactional
	public void deleteByTarget(long postId) {
		feedsDao.deleteAllByPostId(postId);
	}

}
