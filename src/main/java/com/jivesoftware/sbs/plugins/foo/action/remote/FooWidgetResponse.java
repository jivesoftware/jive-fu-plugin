package com.jivesoftware.sbs.plugins.foo.action.remote;

public class FooWidgetResponse extends BaseRemoteResponse {
	private static final long serialVersionUID = -4199771063163476098L;

	private FooWidgetBean bean = null;

	public FooWidgetBean getBean() {	return bean;	}
	public void setBean(FooWidgetBean bean) {
		this.bean = bean;
	} // end get/setBean

} // end class
