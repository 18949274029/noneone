
package noneoneblog.core.persist.service;

/**
 * @author leisure
 */
public interface VerifyService {
    /**
     * 生成验证码
     * @param userId
     * @param target : email mobile
     * @return
     */
    String generateCode(long userId, int type, String target);

    /**
     * 检验验证码有效性
     * @param userId
     * @param code
     * @return token
     */
    String verify(long userId, int type, String code);

    void verifyToken(long userId, int type, String token);
}
