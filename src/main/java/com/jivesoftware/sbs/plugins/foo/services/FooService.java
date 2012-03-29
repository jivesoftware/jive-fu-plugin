package com.jivesoftware.sbs.plugins.foo.services;

public interface FooService {

	public String ping(String message);

	public FooEntity pong(String key, String value);

} // end interface
