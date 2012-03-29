package com.jivesoftware.sbs.plugins.foo.action;

import org.apache.log4j.Logger;

public class JiveFtlAction extends JiveFooActionSupport {
	private static final long serialVersionUID = 3687155516358660379L;
	private static final Logger s_log = Logger.getLogger(JiveFtlAction.class);

	@Override
	public String execute() {
		if (s_log.isDebugEnabled()) { s_log.debug("execute called..."); }
		return SUCCESS;
	} // end execute

	public String ftl() {
		if (s_log.isDebugEnabled()) { s_log.debug("ftl called..."); }
		addActionMessage(getText("foo.action.message",new String[] { "ftl" }));
		return "ftl";
	} // end ftl

} // end class
