package noneoneblog.core.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author leisure
 */
public class FeedsEvent extends ApplicationEvent {
	private static final long serialVersionUID = 3220416026013707101L;

	private long authorId;
    private long postId;

    private int privacy;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public FeedsEvent(Object source) {
        super(source);
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }
}
