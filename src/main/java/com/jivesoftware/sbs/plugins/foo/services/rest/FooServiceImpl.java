package com.jivesoftware.sbs.plugins.foo.services.rest;

import java.util.Date;

import com.jivesoftware.community.DocumentManager;
import com.jivesoftware.community.dwr.RemoteSupport;
import com.jivesoftware.sbs.plugins.foo.services.FooEntity;
import com.jivesoftware.sbs.plugins.foo.services.FooService;

public class FooServiceImpl extends RemoteSupport implements FooService {

	private DocumentManager documentManager = null;

	/************************************************************************************************
	 * NOTE: NEED TO MANUALLY CONFIGURE VIA /spring.xml
	 ************************************************************************************************/
	public void setDocumentManager(DocumentManager documentManager) {
		this.documentManager = documentManager;
	} // end setDocumentManager - SPRING MANAGED

	@Override
	public String ping(String message) {
		if (message != null) {
			return new StringBuffer(message).append(" - ").append(new Date().getTime()).toString();
		} // end if
		return "no message";
	} // end ping

	@Override
	public FooEntity pong(String key, String value) {
		FooEntity foo = new FooEntity();
		foo.setKey(key);
		foo.setValue(value);
		return foo;
	} // end pong

} // end class	