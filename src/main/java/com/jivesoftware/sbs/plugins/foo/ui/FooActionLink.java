package com.jivesoftware.sbs.plugins.foo.ui;

import org.apache.log4j.Logger;

import com.jivesoftware.base.UserManager;
import com.jivesoftware.community.web.component.AbstractActionLink;

public class FooActionLink extends AbstractActionLink {
	private static final Logger s_log = Logger.getLogger(FooActionLink.class);

	private UserManager userManager = null;

	/********************************************************************************************************
	 * Navigation Links are NOT Auto-Wired, you will need to do this yourself in your /spring.xml
	 ********************************************************************************************************/
    public void setUserManager(UserManager userManager) {
		if (s_log.isDebugEnabled()) { s_log.debug("setUserManager called..."); }
        this.userManager = userManager;
    } // end setUserManager - SPRING MANAGED

	/********************************************************************************************************
	 * Defining dynamic logic inside the Java Class.  All other logic that is static, configuring in /spring.xml
	 ********************************************************************************************************/
	@Override
	public boolean isVisible() {
		if (s_log.isDebugEnabled()) { s_log.debug("isVisible called..."); }
		return (!uiComponentContext.getUser().isAnonymous());
	} // end isVisible

} // end class