
package noneoneblog.core.persist.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import noneoneblog.core.data.Comment;
import noneoneblog.core.data.Post;
import noneoneblog.core.data.User;
import noneoneblog.core.persist.dao.CommentDao;
import noneoneblog.core.persist.entity.CommentPO;
import noneoneblog.core.persist.service.CommentService;
import noneoneblog.core.persist.service.PostService;
import noneoneblog.core.persist.service.UserEventService;
import noneoneblog.core.persist.service.UserService;
import noneoneblog.core.persist.utils.BeanMapUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @author leisure
 *
 */
@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private UserService userService;
	@Autowired
	private UserEventService userEventService;
	@Autowired
	private PostService postService;
	
	@Override
	@Transactional(readOnly = true)
	public Page<Comment> paging4Admin(Pageable pageable) {
		Page<CommentPO> page = commentDao.findAll(pageable);
		List<Comment> rets = new ArrayList<>();

		HashSet<Long> uids= new HashSet<>();

		page.getContent().forEach(po -> {
			uids.add(po.getAuthorId());
			rets.add(BeanMapUtils.copy(po));
		});

		buildUsers(rets, uids);

		return new PageImpl<>(rets, pageable, page.getTotalElements());
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "commentsCaches", key = "'lth_' + #authorId + '_' + #pageable.getPageNumber() + '_' + #pageable.getPageSize()")
	public Page<Comment> paging4Home(Pageable pageable, long authorId) {
		Page<CommentPO> page = commentDao.findAllByAuthorIdOrderByCreatedDesc(pageable, authorId);

		List<Comment> rets = new ArrayList<>();
		Set<Long> parentIds = new HashSet<>();
		Set<Long> uids = new HashSet<>();
		Set<Long> postIds = new HashSet<>();

		page.getContent().forEach(po -> {
			Comment c = BeanMapUtils.copy(po);

			if (c.getPid() > 0) {
				parentIds.add(c.getPid());
			}
			uids.add(c.getAuthorId());
			postIds.add(c.getToId());

			rets.add(c);
		});

		// 加载父节点
		if (!parentIds.isEmpty()) {
			Map<Long, Comment> pm = findByIds(parentIds);

			rets.forEach(c -> {
				if (c.getPid() > 0) {
					c.setParent(pm.get(c.getPid()));
				}
			});
		}

		buildUsers(rets, uids);
		buildPosts(rets, postIds);

		return new PageImpl<>(rets, pageable, page.getTotalElements());
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "commentsCaches", key = "'lt_' + #toId + '_' + #pageable.getPageNumber() + '_' + #pageable.getPageSize()")
	public Page<Comment> paging(Pageable pageable, long toId) {
		Page<CommentPO> page = commentDao.findAllByToIdOrderByCreatedDesc(pageable, toId);
		
		List<Comment> rets = new ArrayList<>();
		Set<Long> parentIds = new HashSet<>();
		Set<Long> uids = new HashSet<>();

		page.getContent().forEach(po -> {
			Comment c = BeanMapUtils.copy(po);

			if (c.getPid() > 0) {
				parentIds.add(c.getPid());
			}
			uids.add(c.getAuthorId());

			rets.add(c);
		});

		// 加载父节点
		if (!parentIds.isEmpty()) {
			Map<Long, Comment> pm = findByIds(parentIds);

			rets.forEach(c -> {
				if (c.getPid() > 0) {
					c.setParent(pm.get(c.getPid()));
				}
			});
		}

		buildUsers(rets, uids);

		return new PageImpl<>(rets, pageable, page.getTotalElements());
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Long, Comment> findByIds(Set<Long> ids) {
		List<CommentPO> list = commentDao.findByIdIn(ids);
		Map<Long, Comment> ret = new HashMap<>();
		Set<Long> uids = new HashSet<>();

		list.forEach(po -> {
			uids.add(po.getAuthorId());
			ret.put(po.getId(), BeanMapUtils.copy(po));
		});

		buildUsers(ret.values(), uids);
		return ret;
	}

	@Override
	@Transactional
	@CacheEvict(value = "commentsCaches", allEntries = true)
	public long post(Comment comment) {
		CommentPO po = new CommentPO();
		
		po.setAuthorId(comment.getAuthorId());
		po.setToId(comment.getToId());
		po.setContent(comment.getContent());
		po.setCreated(new Date());
		po.setPid(comment.getPid());
		commentDao.save(po);

		userEventService.identityComment(comment.getAuthorId(), po.getId(), true);
		return po.getId();
	}

	@Override
	@Transactional
	@CacheEvict(value = "commentsCaches", allEntries = true)
	public void delete(List<Long> ids) {
		commentDao.deleteAllByIdIn(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "commentsCaches", allEntries = true)
	public void delete(long id, long authorId) {
		CommentPO po = commentDao.findOne(id);
		if (po != null) {
			// 判断文章是否属于当前登录用户
			Assert.isTrue(po.getAuthorId() == authorId, "认证失败");
			commentDao.delete(po);
		}
	}

	@Transactional
	@Override
	public List<CommentPO> findAllByAuthorIdAndToId(long authorId, long toId) {
		return commentDao.findAllByAuthorIdAndToIdOrderByCreatedDesc(authorId, toId);
	}

	private void buildUsers(Collection<Comment> posts, Set<Long> uids) {
		Map<Long, User> userMap = userService.findMapByIds(uids);

		posts.forEach(p -> p.setAuthor(userMap.get(p.getAuthorId())));
	}

	private void buildPosts(Collection<Comment> comments, Set<Long> postIds) {
		Map<Long, Post> postMap = postService.findSingleMapByIds(postIds);

		comments.forEach(p -> p.setPost(postMap.get(p.getToId())));
	}

}
