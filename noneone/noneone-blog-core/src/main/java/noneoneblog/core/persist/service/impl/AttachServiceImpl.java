
package noneoneblog.core.persist.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import noneoneblog.core.data.Attach;
import noneoneblog.core.persist.dao.AttachDao;
import noneoneblog.core.persist.entity.AttachPO;
import noneoneblog.core.persist.service.AttachService;
import noneoneblog.core.persist.utils.BeanMapUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author leisure
 *
 */
@Service
public class AttachServiceImpl implements AttachService {
	@Autowired
	private AttachDao attachDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Attach> findByTarget(long toId) {
		List<AttachPO> list = attachDao.findAllByToId(toId);
		List<Attach> rets = new ArrayList<>();
		list.forEach(po -> rets.add(BeanMapUtils.copy(po)));
		return rets;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Map<Long, List<Attach>> findByTarget(Set<Long> toIds) {
		if (toIds == null || toIds.isEmpty()) {
			return Collections.emptyMap();
		}

		List<AttachPO> list = attachDao.findAllByToIdIn(toIds);
		Map<Long, List<Attach>> ret = new HashMap<>();

		list.forEach(po -> {
			Attach a = BeanMapUtils.copy(po);

			List<Attach> ats = ret.get(a.getToId());

			if (ats == null) {
				ats = new ArrayList<>();
				ret.put(a.getToId(), ats);
			}
			ats.add(a);
		});
		
		return ret;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Map<Long, Attach> findByIds(Set<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyMap();
		}

		List<AttachPO> list = attachDao.findAllByToIdIn(ids);
		Map<Long, Attach> ret = new HashMap<>();

		list.forEach(po -> ret.put(po.getId(), BeanMapUtils.copy(po)));
		return ret;
	}

	
	@Override
	@Transactional
	public long add(Attach album) {
		AttachPO po = new AttachPO();
		BeanUtils.copyProperties(album, po);
		attachDao.save(po);
		return po.getId();
	}
	
	@Override
	@Transactional
	public long batchPost(long toId, List<Attach> albums) {
		long lastId = 0;

		attachDao.deleteAllByToId(toId);

		for (Attach d : albums) {
			if (StringUtils.isNotEmpty(d.getOriginal())&&d.getOriginal().startsWith("http")) {
				d.setToId(toId);
				lastId = add(d);
			}
		}
//		attachDao.batchAdd(albums);
		return lastId;
	}
	
	@Override
	@Transactional
	public void deleteByToId(long toId) {
		attachDao.deleteAllByToId(toId);
	}

	@Override
	@Transactional
	public void delete(long id) {
		attachDao.delete(id);
	}

}
