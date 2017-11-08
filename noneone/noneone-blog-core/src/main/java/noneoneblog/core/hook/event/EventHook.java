package noneoneblog.core.hook.event;

import noneoneblog.core.hook.Hook;

import org.springframework.context.ApplicationEvent;

/**
 * Event钩子
 *
 * @author leisure
 */
public interface EventHook<T extends ApplicationEvent> extends Hook {
    /**
     * Event监听
     *
     * @param event
     */
    void onApplicationEvent(T event);

    /**
     * 获取Event类做做key
     *
     * @return
     */
    Class getEventClass();
}
