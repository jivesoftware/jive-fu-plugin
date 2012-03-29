package com.jivesoftware.sbs.plugins.foo.ui;

import org.apache.log4j.Logger;

import com.jivesoftware.community.navbar.NavMenuLink;

public class FooMenuLink extends NavMenuLink {
	private static final Logger s_log = Logger.getLogger(FooMenuLink.class);

	/********************************************************************************************************
	 * Defining dynamic logic inside the Java Class.  All other logic that is static, configuring in /spring.xml
	 ********************************************************************************************************/
	@Override
	public boolean isVisible() {
		if (s_log.isDebugEnabled()) { s_log.debug("isVisible called..."); }
		return (!uiComponentContext.getUser().isAnonymous());
	} // end isVisible
} // end class