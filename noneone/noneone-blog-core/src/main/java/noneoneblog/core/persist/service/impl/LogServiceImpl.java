
package noneoneblog.core.persist.service.impl;

import java.util.Date;

import noneoneblog.core.persist.dao.LogDao;
import noneoneblog.core.persist.entity.LogPO;
import noneoneblog.core.persist.service.LogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author leisure
 *
 */
@Service
public class LogServiceImpl implements LogService {
	@Autowired
	private LogDao logDao;
	
	@Override
	@Transactional
	public void add(int logType, long userId, long targetId, String ip) {
		LogPO po = new LogPO();
		po.setType(logType);
		po.setTargetId(targetId);
		po.setUserId(userId);
		po.setIp(ip);
		po.setCreated(new Date());
		logDao.save(po);
	}
	
}
