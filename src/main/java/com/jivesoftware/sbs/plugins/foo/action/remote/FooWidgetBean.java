package com.jivesoftware.sbs.plugins.foo.action.remote;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.jivesoftware.base.User;
import com.jivesoftware.util.DateUtils;

public class FooWidgetBean implements Serializable {
	private static final long serialVersionUID = 311889122927841381L;

	private String key = null;
	private String value = null;
	private long timestamp = -1;
	private DateUtils dateUtils = null;

	public FooWidgetBean() {
		/*** NEEDED FOR FRAMEWORK CONSTRUCTION ***/
		timestamp = new Date().getTime();
	} // end constructor

	public FooWidgetBean(HttpServletRequest request, User user) {
		dateUtils = new DateUtils(request,user);
	} // end constructor

	public long getTimestamp() {	return timestamp;	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	} // end get/setTimestamp

	public String getKey() {	return key;	}
	public void setKey(String key) {
		this.key = key;
	} // end get/setKey

	public String getValue() {	return value; }
	public void setValue(String value) {
		this.value = value;
	} // end get/setValue

} // end class