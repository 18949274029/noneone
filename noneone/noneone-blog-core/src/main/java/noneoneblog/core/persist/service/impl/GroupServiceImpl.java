
package noneoneblog.core.persist.service.impl;

import java.util.ArrayList;
import java.util.List;

import noneoneblog.base.lang.Consts;
import noneoneblog.core.data.Group;
import noneoneblog.core.persist.dao.GroupDao;
import noneoneblog.core.persist.entity.GroupPO;
import noneoneblog.core.persist.service.GroupService;
import noneoneblog.core.persist.utils.BeanMapUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author leisure
 *
 */
@Service
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {
	@Autowired
	private GroupDao groupDao;

	@Override
	public List<Group> findAll(int status) {
		List<GroupPO> list;
		if (status > Consts.IGNORE) {
			list = groupDao.findAllByStatus(status);
		} else {
			list = groupDao.findAll();
		}
		List<Group> rets = new ArrayList<>();
		list.forEach(po -> rets.add(BeanMapUtils.copy(po)));
		return rets;
	}

	@Override
	@Cacheable(value = "groupsCaches", key = "'g_' + #id")
	public Group getById(int id) {
		return BeanMapUtils.copy(groupDao.findOne(id));
	}

	@Override
	@Cacheable(value = "groupsCaches", key = "'g_' + #key")
	public Group getByKey(String key) {
		return BeanMapUtils.copy(groupDao.findByKey(key));
	}

	@Override
	@Transactional
	public void update(Group group) {
		GroupPO po = groupDao.findOne(group.getId());
		if (po != null) {
			BeanUtils.copyProperties(group, po);
		} else {
			po = new GroupPO();
			BeanUtils.copyProperties(group, po);
			groupDao.save(po);
		}
	}

	@Override
	@Transactional
	public void delete(int id) {
		groupDao.delete(id);
	}

}
