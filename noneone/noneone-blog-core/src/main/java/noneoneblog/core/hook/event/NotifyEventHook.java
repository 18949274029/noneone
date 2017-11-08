package noneoneblog.core.hook.event;

import noneoneblog.core.event.NotifyEvent;

import org.springframework.stereotype.Component;


/**
 * 处理LogEvent钩子
 *
 * @author leisure
 */
@Component
public class NotifyEventHook extends EventHookSupport<NotifyEvent> {
    @Override
    public void init() {
        this.plugins = getPlugins(NotifyEventListener.class);
    }

    @Override
    public void onApplicationEvent(NotifyEvent event) {
    }

    public interface NotifyEventListener extends EventListener<NotifyEvent> {

    }

}
