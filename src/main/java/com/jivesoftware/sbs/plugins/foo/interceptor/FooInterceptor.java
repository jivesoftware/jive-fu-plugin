package com.jivesoftware.sbs.plugins.foo.interceptor;

/*
 * Copyright (C) 2012 LG. All rights reserved.
 *
 */

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.jivesoftware.community.JiveContentObject;
import com.jivesoftware.community.JiveInterceptor;
import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.RejectedException;
import com.jivesoftware.community.annotations.PropertyNames;
import com.jivesoftware.community.moderation.ModerationUtil;

/****
 * After start-up we need to add the interceptor manually in the admin GUI.
 * Select "Spaces",
 * "Settings, "Interceptors", enter "com.jivesoftware.sbs.plugins.foo.interceptor.FooInterceptor" (replace again "Foo") and click on "Add Interceptor".
 * Now we can install and configure it. After configuration create a new discussion. After clicking on "Post Message" Jive SBS should display
 * "Please note, your discussion will need to be approved by a moderator before it will be viewable by others.".
 *
 * Let's verify the log file, there we should find a line like this:
 * "ERROR com.jivesoftware.sbs.plugins.foo.interceptor.FooInterceptor - Content of admin will be moderated because of FooInterceptor".
 * The user name and the end of the log message vary depending on the user and your interceptor configuration.
 */

@PropertyNames({ "interceptorEnabled", "logString" })
public class FooInterceptor implements JiveInterceptor {
	private static Logger log = LogManager.getLogger(FooInterceptor.class);

	// variables to store the property values
	private boolean interceptorEnabled = true;
	private String logString = "FootInterceptor";

	// getters and setters
	// for boolean value: interceptorEnabled:
	// boolean=isInterceptorEnabled() + setInterceptorEnabled(boolean)
	public final boolean isInterceptorEnabled() {
		return interceptorEnabled;
	}

	public final void setInterceptorEnabled(boolean interceptorEnabled) {
		this.interceptorEnabled = interceptorEnabled;
	}

	// for string value: logString:
	// String=getLogString() + setLogString(String)
	public final String getLogString() {
		return logString;
	}

	public final void setLogString(String logString) {
		if (logString != null) {
			this.logString = logString;
		}
	}

	// business logic
	@Override
	public final void invokeInterceptor(final JiveObject object, final Type type) throws RejectedException {
		if (interceptorEnabled == false) {
			log.error("Interceptor disabled.");
			return;
		}
		if (object instanceof JiveContentObject) {
			JiveContentObject content = (JiveContentObject) object;
			String userName = content.getUser().getUsername();
			log.error("Content of " + userName + " will be moderated because of " + logString);
			ModerationUtil.forceContentObjectModeration(content);
		}
	}

	// other methods
	@Override
	public final List<Type> getTypes() {
		// Type.TYPE_PRE can not be moderated
		return Lists.newArrayList(Type.TYPE_POST, Type.TYPE_EDIT);
	}

	@Override
	public final boolean isSystemLevel() {
		return false;
	}

	/**
	 * no support for cloning
	 */
	public final Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public FooInterceptor() {
	}

	public FooInterceptor(int objectType, long objectID) {
	}

}
