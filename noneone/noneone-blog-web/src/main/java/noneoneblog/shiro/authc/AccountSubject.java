
package noneoneblog.shiro.authc;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import noneoneblog.core.data.AccountProfile;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;

public class AccountSubject extends WebDelegatingSubject{
	private AccountProfile profile;
    
    public AccountSubject(PrincipalCollection principals, boolean authenticated, String host, Session session,
            boolean sessionEnabled, ServletRequest request, ServletResponse response, SecurityManager securityManager, AccountProfile profile) {
        super(principals, authenticated, host, session, sessionEnabled, request, response, securityManager);
        this.profile = profile;
    }

    public String getUsername(){
        return getPrincipal().toString();
    }

	public AccountProfile getProfile() {
		return profile;
	}
    
}
