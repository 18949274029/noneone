
package noneoneblog.core.persist.service;

import java.util.Date;

/**
 * @author leisure
 *
 */
public interface LogService {
	void add(int logType, long userId, long targetId, String ip);
}
