
package noneoneblog.core.persist.service;

import java.util.List;

import noneoneblog.core.data.Group;

/**
 * TODO: 暂时添加修改都在数据库操作
 * 
 * @author leisure
 *
 */
public interface GroupService {
	List<Group> findAll(int status);
	Group getById(int id);
	Group getByKey(String key);
	void update(Group group);
	void delete(int id);
}
