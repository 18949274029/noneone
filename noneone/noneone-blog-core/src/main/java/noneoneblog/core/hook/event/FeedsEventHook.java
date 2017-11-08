package noneoneblog.core.hook.event;

import noneoneblog.core.event.FeedsEvent;

import org.springframework.stereotype.Component;


/**
 * 处理FeedsEvent钩子
 *
 * @author leisure
 */
@Component
public class FeedsEventHook extends EventHookSupport<FeedsEvent> {

    /**
     * 初始化时获取该钩子的插件
     */
    @Override
    public void init() {
        this.plugins = getPlugins(FeedsEventListener.class);
    }

    @Override
    public void onApplicationEvent(FeedsEvent event) {
        super.onEvent(event);
    }


    public interface FeedsEventListener extends EventListener<FeedsEvent> {

    }

}
