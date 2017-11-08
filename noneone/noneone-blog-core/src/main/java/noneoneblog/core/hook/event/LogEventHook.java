package noneoneblog.core.hook.event;

import noneoneblog.core.event.LogEvent;

import org.springframework.stereotype.Component;


/**
 * 处理LogEvent钩子
 * @author leisure
 */
@Component
public class LogEventHook extends EventHookSupport<LogEvent> {

    @Override
    public void init() {
        this.plugins = getPlugins(LogEventListener.class);
    }

    @Override
    public void onApplicationEvent(LogEvent event) {
        super.onEvent(event);
    }

    public interface LogEventListener extends EventListener<LogEvent> {

    }


}
