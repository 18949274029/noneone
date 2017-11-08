package noneoneblog.core.event.handler;

import java.text.MessageFormat;

import noneoneblog.base.lang.Consts;
import noneoneblog.core.data.Feeds;
import noneoneblog.core.event.FeedsEvent;
import noneoneblog.core.persist.service.FeedsService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author leisure
 */
@Component
public class FeedsEventHandler implements ApplicationListener<FeedsEvent> {
    private Logger log = Logger.getLogger(getClass());

    @Autowired
    private FeedsService feedsService;

    @Async
    @Override
    public void onApplicationEvent(FeedsEvent event) {
        if (event == null) {
            return;
        }

        // 创建动态对象
        Feeds feeds = new Feeds();
        feeds.setType(Consts.FEEDS_TYPE_POST);
        feeds.setOwnId(event.getAuthorId());
        feeds.setPostId(event.getPostId());
        feeds.setAuthorId(event.getAuthorId());

        int ret = feedsService.add(feeds);

        log.debug(MessageFormat.format("成功派发 {0} 条动态!", ret));
    }
}
