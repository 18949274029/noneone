
package noneoneblog.core.event.handler;

import noneoneblog.base.lang.EnumLog;
import noneoneblog.core.event.LogEvent;
import noneoneblog.core.persist.service.LogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author leisure
 *
 */
@Component
public class LogEventHandler implements ApplicationListener<LogEvent> {
	@Autowired
	private LogService logService;
	
	@Override
	public void onApplicationEvent(LogEvent event) {
		EnumLog type = event.getType();
		
		switch (type) {
			case FAVORED:
				logService.add(type.getIndex(), event.getUserId(), event.getTargetId(), event.getIp());
				break;
			case BROWSE:
				break;
			default:
				break;
		}
	}


}
