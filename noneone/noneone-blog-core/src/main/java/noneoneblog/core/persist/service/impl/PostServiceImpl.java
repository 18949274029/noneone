
package noneoneblog.core.persist.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;

import noneoneblog.base.lang.Consts;
import noneoneblog.base.lang.EntityStatus;
import noneoneblog.base.utils.PreviewTextUtils;
import noneoneblog.core.data.Attach;
import noneoneblog.core.data.Post;
import noneoneblog.core.data.User;
import noneoneblog.core.persist.dao.PostAttributeDao;
import noneoneblog.core.persist.dao.PostDao;
import noneoneblog.core.persist.entity.PostAttribute;
import noneoneblog.core.persist.entity.PostPO;
import noneoneblog.core.persist.service.AttachService;
import noneoneblog.core.persist.service.FavorService;
import noneoneblog.core.persist.service.PostService;
import noneoneblog.core.persist.service.UserEventService;
import noneoneblog.core.persist.service.UserService;
import noneoneblog.core.persist.utils.BeanMapUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PostServiceImpl implements PostService {
	@Autowired
	private PostDao postDao;
	@Autowired
	private AttachService attachService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserEventService userEventService;
	@Autowired
	private FavorService favorService;
	@Autowired
	private PostAttributeDao postAttributeDao;

	@Override
	@Transactional(readOnly = true)
	public Page<Post> paging(Pageable pageable, int group, String ord, boolean whetherHasAlbums) {
		Page<PostPO> page = postDao.findAll((root, query, builder) -> {

			List<Order> orders = new ArrayList<>();
			orders.add(builder.desc(root.<Long>get("featured")));
			if(Consts.order.NEWEST.equals(ord)){
				orders.add(builder.desc(root.<Long>get("created")));
			}

			Predicate predicate = builder.conjunction();

			if (group > Consts.ZERO) {
				predicate.getExpressions().add(
						builder.equal(root.get("group").as(Integer.class), group));
			}

			if (Consts.order.HOTTEST.equals(ord)) {
				orders.add(builder.desc(root.<Long>get("views")));
			}

			query.orderBy(orders);

			return predicate;
		}, pageable);

		return new PageImpl<>(toPosts(page.getContent(), whetherHasAlbums), pageable, page.getTotalElements());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Post> paging4Admin(Pageable pageable, long id, String title, int group) {
		Page<PostPO> page = postDao.findAll((root, query, builder) -> {
            query.orderBy(
					builder.desc(root.<Long>get("featured")),
					builder.desc(root.<Long>get("created"))
			);

            Predicate predicate = builder.conjunction();

			if (group > Consts.ZERO) {
				predicate.getExpressions().add(
						builder.equal(root.get("group").as(Integer.class), group));
			}

			if (StringUtils.isNotBlank(title)) {
				predicate.getExpressions().add(
						builder.like(root.get("title").as(String.class), "%" + title + "%"));
			}

			if (id > Consts.ZERO) {
				predicate.getExpressions().add(
						builder.equal(root.get("id").as(Integer.class), id));
			}

            return predicate;
        }, pageable);

		return new PageImpl<>(toPosts(page.getContent(), false), pageable, page.getTotalElements());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Post> pagingByAuthorId(Pageable pageable, long userId) {
		Page<PostPO> page = postDao.findAllByAuthorIdOrderByCreatedDesc(pageable, userId);
		return new PageImpl<>(toPosts(page.getContent(), true), pageable, page.getTotalElements());
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Post> search(Pageable pageable, String q) throws Exception {
		Page<Post> page = postDao.search(pageable, q);

		HashSet<Long> ids = new HashSet<>();
		HashSet<Long> uids = new HashSet<>();

		for (Post po : page.getContent()) {
			ids.add(po.getId());
			uids.add(po.getAuthorId());
		}

		// 加载相册
		buildAttachs(page.getContent(), ids);

		// 加载用户信息
		buildUsers(page.getContent(), uids);

		return page;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Post> searchByTag(Pageable pageable, String tag) {
		Page<Post> page = postDao.searchByTag(pageable, tag);

		HashSet<Long> ids = new HashSet<>();
		HashSet<Long> uids = new HashSet<>();

		for (Post po : page.getContent()) {
			ids.add(po.getId());
			uids.add(po.getAuthorId());
		}

		// 加载相册
		buildAttachs(page.getContent(), ids);

		// 加载用户信息
		buildUsers(page.getContent(), uids);
		return page;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Post> findLatests(int maxResults, long ignoreUserId) {
		List<PostPO> list = postDao.findTop9ByOrderByCreatedDesc();
		List<Post> rets = new ArrayList<>();

		list.forEach(po -> rets.add(BeanMapUtils.copy(po, 0)));

		return rets;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Post> findHots(int maxResults, long ignoreUserId) {
		List<PostPO> list = postDao.findTop9ByOrderByViewsDesc();
		List<Post> rets = new ArrayList<>();

		list.forEach(po -> rets.add(BeanMapUtils.copy(po, 0)));
		return rets;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Map<Long, Post> findSingleMapByIds(Set<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyMap();
		}

		List<PostPO> list = postDao.findAllByIdIn(ids);
		Map<Long, Post> rets = new HashMap<>();

		HashSet<Long> imageIds = new HashSet<>();
		HashSet<Long> uids = new HashSet<>();

		list.forEach(po -> {
			rets.put(po.getId(), BeanMapUtils.copy(po, 0));

			// 此处加载最后一张图片
			if (po.getLastImageId() > 0) {
				imageIds.add(po.getLastImageId());
			}

			uids.add(po.getAuthorId());
		});
		
		Map<Long, Attach> ats = attachService.findByIds(imageIds);

		rets.forEach((k, v) -> {
			if (v.getLastImageId() > 0) {
				Attach a = ats.get(v.getLastImageId());
				v.setAlbum(a);
			}
		});

		// 加载用户信息
		buildUsers(rets.values(), uids);

		return rets;
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Long, Post> findMultileMapByIds(Set<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyMap();
		}

		List<PostPO> list = postDao.findAllByIdIn(ids);
		Map<Long, Post> rets = new HashMap<>();

		HashSet<Long> uids = new HashSet<>();

		list.forEach(po -> {
			rets.put(po.getId(), BeanMapUtils.copy(po, 0));
			uids.add(po.getAuthorId());
		});

		// 加载相册
		buildAttachs(rets.values(), ids);

		// 加载用户信息
		buildUsers(rets.values(), uids);

		return rets;
	}

	@Override
	@Transactional
	public long post(Post post) {
		PostPO po = new PostPO();

		BeanUtils.copyProperties(post, po);

		po.setCreated(new Date());
		po.setStatus(EntityStatus.ENABLED);
        post.setContent(post.getContent()==null?null:PreviewTextUtils.getChangeHTML(post.getContent()));
		// 处理摘要
		if (StringUtils.isBlank(post.getSummary())) {
			po.setSummary(trimSummary(post.getContent()));
		} else {
			po.setSummary(post.getSummary());
		}

		postDao.save(po);

		PostAttribute attr = new PostAttribute();
		attr.setContent(post.getContent());
		attr.setId(po.getId());
		submitAttr(attr);
		
		// 处理相册
		if (post.getAlbums() != null) {
			long lastImageId = attachService.batchPost(po.getId(), post.getAlbums());
			po.setLastImageId(lastImageId);
			po.setImages(post.getAlbums().size());
		}
		
		// 更新文章统计
		userEventService.identityPost(po.getAuthorId(), po.getId(), true);

		return po.getId();
	}
	
	@Override
	@Transactional
	public Post get(long id) {
		PostPO po = postDao.findOne(id);
		Post d = null;
		if (po != null) {
			d = BeanMapUtils.copy(po, 1);

			d.setAuthor(userService.get(d.getAuthorId()));
			d.setAlbums(attachService.findByTarget(d.getId()));

			PostAttribute attr = postAttributeDao.findOne(po.getId());
			if (attr != null) {
				d.setContent(attr.getContent());
			}
		}
		return d;
	}

	/**
	 * 更新文章方法
	 * @param p
	 */
	@Override
	@Transactional
	public void update(Post p){
		PostPO po = postDao.findOne(p.getId());

		if (po != null) {
			po.setTitle(p.getTitle());//标题
			po.setGroup(p.getGroup());

			// 处理摘要
			if (StringUtils.isBlank(p.getSummary())) {
				po.setSummary(trimSummary(p.getContent()));
			} else {
				po.setSummary(p.getSummary());
			}

			po.setTags(p.getTags());//标签

			// 处理相册
			if (p.getAlbums() != null && !p.getAlbums().isEmpty()) {
				long lastImageId = attachService.batchPost(po.getId(), p.getAlbums());
				po.setLastImageId(lastImageId);
				po.setImages(po.getImages() + p.getAlbums().size());
			}

			// 保存扩展
			PostAttribute attr = new PostAttribute();
			attr.setContent(p.getContent());
			attr.setId(po.getId());
			submitAttr(attr);
		}
	}

	@Override
	@Transactional
	public void updateFeatured(long id, int featured) {
		PostPO po = postDao.findOne(id);

		if (po != null) {
			int max = featured;
			if (Consts.FEATURED_ACTIVE == featured) {
				max = postDao.maxFeatured() + 1;
			}
			po.setFeatured(max);
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		PostPO po = postDao.findOne(id);
		if (po != null) {
			attachService.deleteByToId(id);
			postDao.delete(po);
		}
	}
	
	@Override
	@Transactional
	public void delete(long id, long authorId) {
		PostPO po = postDao.findOne(id);
		if (po != null) {
			// 判断文章是否属于当前登录用户
			Assert.isTrue(po.getAuthorId() == authorId, "认证失败");

			attachService.deleteByToId(id);
			postDao.delete(po);
		}
	}
	
	@Override
	@Transactional
	public void identityViews(long id) {
		PostPO po = postDao.findOne(id);
		if (po != null) {
			po.setViews(po.getViews() + Consts.IDENTITY_STEP);
		}
	}

	@Override
	@Transactional
	public void identityComments(long id) {
		PostPO po = postDao.findOne(id);
		if (po != null) {
			po.setComments(po.getComments() + Consts.IDENTITY_STEP);
		}
	}

	@Override
	@Transactional
	public void favor(long userId, long postId) {
		PostPO po = postDao.findOne(postId);

		Assert.notNull(po, "文章不存在");

		favorService.add(userId, postId);

		po.setFavors(po.getFavors() + Consts.IDENTITY_STEP);
	}

	@Override
	@Transactional
	public void unfavor(long userId, long postId) {
		PostPO po = postDao.findOne(postId);

		Assert.notNull(po, "文章不存在");

		favorService.delete(userId, postId);

		po.setFavors(po.getFavors() - Consts.IDENTITY_STEP);
	}
	
	@Override
	@Transactional
	public void resetIndexs() {
		postDao.resetIndexs();
	}

	/**
	 * 截取文章内容
	 * @param text
	 * @return
	 */
	private String trimSummary(String text){
		return PreviewTextUtils.getText(text, 126);
	}

	private List<Post> toPosts(List<PostPO> posts, boolean whetherHasAlbums) {
		List<Post> rets = new ArrayList<>();

		HashSet<Long> pids = new HashSet<>();
		HashSet<Long> uids = new HashSet<>();

		posts.forEach(po -> {
			pids.add(po.getId());
			uids.add(po.getAuthorId());

			rets.add(BeanMapUtils.copy(po, 0));
		});

		// 加载用户信息
		buildUsers(rets, uids);

		// 判断是否加载相册
		if (whetherHasAlbums) {
			buildAttachs(rets, pids);
		}
		return rets;
	}

	private void buildAttachs(Collection<Post> posts, Set<Long> postIds) {
    	Map<Long, List<Attach>> attMap = attachService.findByTarget(postIds);

		posts.forEach(p -> p.setAlbums(attMap.get(p.getId())));
    }

	private void buildUsers(Collection<Post> posts, Set<Long> uids) {
		Map<Long, User> userMap = userService.findMapByIds(uids);

		posts.forEach(p -> p.setAuthor(userMap.get(p.getAuthorId())));
	}

	private void submitAttr(PostAttribute attr) {
		postAttributeDao.save(attr);
	}

	@Override
	public Post findPost(String title) {
		List<PostPO> po = postDao.findByTitle(title);
		if (po != null && po.size() > 0) {
			Post post = new Post();
			BeanUtils.copyProperties(po.get(0), post);
			return post;
		}
		return null;

	}

	@Override
	public List<BigInteger> getIDsRTId(Long start,Long end) {
		return postDao.getIDsRTId(start,end);
	}

}
