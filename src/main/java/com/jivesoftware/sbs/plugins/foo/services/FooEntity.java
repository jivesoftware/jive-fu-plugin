package com.jivesoftware.sbs.plugins.foo.services;

import java.io.Serializable;

public class FooEntity implements Serializable {
	private static final long serialVersionUID = -8080924721435762841L;

	private String key = null;
	private String value = null;

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

} // end class
