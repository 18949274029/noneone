package noneoneblog.core.event.handler;

import noneoneblog.base.lang.Consts;
import noneoneblog.core.biz.PostBiz;
import noneoneblog.core.data.Notify;
import noneoneblog.core.data.Post;
import noneoneblog.core.event.NotifyEvent;
import noneoneblog.core.persist.service.NotifyService;
import noneoneblog.core.persist.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author leisure
 */
@Component
public class NotifyEventHandler implements ApplicationListener<NotifyEvent> {
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private PostBiz postPlanet;
    @Autowired
    private PostService postService;

    @Async
    @Override
    public void onApplicationEvent(NotifyEvent event) {
        Notify nt = new Notify();
        nt.setPostId(event.getPostId());
        nt.setFromId(event.getFromUserId());
        nt.setEvent(event.getEvent());

        switch (event.getEvent()) {
            case Consts.NOTIFY_EVENT_FAVOR_POST:
                Post p = postPlanet.getPost(event.getPostId());
                nt.setOwnId(p.getAuthorId());
                break;
            case Consts.NOTIFY_EVENT_COMMENT:
            case Consts.NOTIFY_EVENT_COMMENT_REPLY:
                Post p2 = postPlanet.getPost(event.getPostId());
                nt.setOwnId(p2.getAuthorId());

                // 自增评论数
                postService.identityComments(event.getPostId());

                break;
            default:
                nt.setOwnId(event.getToUserId());
        }

        notifyService.send(nt);
    }
}
