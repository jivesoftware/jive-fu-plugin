package com.jivesoftware.sbs.plugins.foo.services.soap;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.jivesoftware.community.DocumentManager;
import com.jivesoftware.sbs.plugins.foo.services.FooEntity;
import com.jivesoftware.sbs.plugins.foo.services.FooService;

@WebService(portName = "JiveFooServicePort", serviceName = "JiveFooService", targetNamespace = "http://jivesoftware.com/plugins/foo/webservices")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT,  use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class FooServiceImpl implements FooService {

	private DocumentManager documentManager = null;

	/************************************************************************************************
	 * NOTE: NEED TO MANUALLY CONFIGURE VIA /spring.xml
	 ************************************************************************************************/
	public void setDocumentManager(DocumentManager documentManager) {
		this.documentManager = documentManager;
	} // end setDocumentManager - SPRING MANAGED

	@WebMethod
	public String ping(String message) {
		if (message != null) {
			return new StringBuffer(message).append(" - ").append(new Date().getTime()).toString();
		} // end if
		return "no message";
	} // end getFoo


	@WebMethod
	public FooEntity pong(String key,String value) {
		FooEntity foo = new FooEntity();
		foo.setKey(key);
		foo.setValue(value);
		return foo;
	} // end echo

} // end class
