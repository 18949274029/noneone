package noneoneblog.core.persist.dao.custom;

import noneoneblog.core.data.Feeds;

/**
 * Created by leisure
 */
public interface FeedsDaoCustom {
    /**
     * 添加动态, 同时会分发给粉丝
     *
     * @param feeds
     * @return
     */
    int batchAdd(Feeds feeds);
}
