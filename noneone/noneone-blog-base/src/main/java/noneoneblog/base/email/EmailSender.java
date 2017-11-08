package noneoneblog.base.email;

import java.util.Map;

/**
 * @author leisure 
 */
public interface EmailSender {

    void sendTemplete(String address, String subject, String template, Map<String, Object> data);

    void sendText(String address, String subject, String content, boolean html);
}
