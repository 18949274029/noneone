package noneoneblog.plugin.example;

import noneoneblog.core.event.FeedsEvent;
import noneoneblog.core.hook.event.FeedsEventHook;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
/**
 * @author leisure
 */
@Component
public class FeedsEventPlugin implements FeedsEventHook.FeedsEventListener {
    private Logger log = Logger.getLogger(getClass());
    @Override
    public void onEvent(FeedsEvent event) {
        log.debug("插件发出来的消息:您又派发动态了");
    }
}
