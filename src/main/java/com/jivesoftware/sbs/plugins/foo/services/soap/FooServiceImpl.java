package com.jivesoftware.sbs.plugins.foo.services.soap;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.codehaus.jra.Get;
import org.codehaus.jra.HttpResource;

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
	@Get
	@HttpResource(location = "/ping")
	@Override
	public String ping(
			@WebParam(name = "message")String message) {
		if (message != null) {
			return new StringBuffer(message).append(" - ").append(new Date().getTime()).toString();
		} // end if
		return "no message";
	} // end getFoo


	@WebMethod
	@Get
	@HttpResource(location = "/pong")
	@Override
	public FooEntity pong(
			@WebParam(name = "key")String key,
			@WebParam(name = "value")String value) {
		FooEntity foo = new FooEntity();
		foo.setKey(key);
		foo.setValue(value);
		return foo;
	} // end echo

} // end class
