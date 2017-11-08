package noneoneblog.core.persist.service;

import java.util.List;

import noneoneblog.core.data.Role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
	
	Page<Role> paging(Pageable pageable);
	
	Role get(Long id);
	
	void save(Role role);
	
	void delete(Long id);
	
	List<Role> getAll();

}
