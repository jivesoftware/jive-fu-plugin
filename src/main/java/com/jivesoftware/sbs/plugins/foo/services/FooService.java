package com.jivesoftware.sbs.plugins.foo.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.codehaus.jra.Get;
import org.codehaus.jra.HttpResource;

/**
 * SOAP Annotations - WebService, SOAPBinding, WebMethod, Get, HttpResource, WebParam
 * REST Annotations - Produces, GET, Path, FormParam, PathParam
 *
 */

@WebService(portName = "JiveFooServicePort", serviceName = "JiveFooService", targetNamespace = "http://jivesoftware.com/plugins/foo/webservices")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT,  use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Produces("application/json")
public interface FooService {

    @WebMethod
	@Get
	@HttpResource(location = "/ping")
    @GET
    @Path("/ping")
	public String ping(
			@WebParam(name = "message") @FormParam("message") String message
			);

    @WebMethod
	@Get
	@HttpResource(location = "/pong")
    @GET
    @Path("/pong")
	public FooEntity pong(
			@WebParam(name = "key") @FormParam("key") String key,
			@WebParam(name = "value") @FormParam("value") String value
			);

} // end interface
