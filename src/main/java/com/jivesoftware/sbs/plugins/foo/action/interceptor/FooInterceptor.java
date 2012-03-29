package com.jivesoftware.sbs.plugins.foo.action.interceptor;

import org.apache.log4j.Logger;

import com.jivesoftware.base.aaa.AuthenticationProvider;
import com.jivesoftware.base.aaa.JiveAuthentication;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class FooInterceptor implements Interceptor {
	private static final long serialVersionUID = -7118259459484686680L;

	private static final Logger s_log = Logger.getLogger(FooInterceptor.class);

	private AuthenticationProvider authenticationProvider;

    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    } // end setAuthenticationProvider - SPRING MANAGED

	public void destroy() {
		if (s_log.isDebugEnabled()) { s_log.debug("destroy called..."); }
	} // end destroy

    public void init() {
    	if (s_log.isDebugEnabled()) { s_log.debug("init called..."); }
    } // end init

    public String intercept(ActionInvocation ai) throws Exception {
    	if (s_log.isDebugEnabled()) { s_log.debug("intercept called..."); }

        // Get the user's authentication
       JiveAuthentication authentication = authenticationProvider.getAuthentication();

       /*** EXIT EARLY WITH A KNOWN RESULT ***/
       if(authentication == null || authentication.isAnonymous()) {
           return "login";
       } // end if

       /*** ALLOW HIT THROUGH ***/
       return ai.invoke();
    } // end intercept

} // end class