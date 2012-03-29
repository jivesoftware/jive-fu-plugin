package com.jivesoftware.sbs.plugins.foo.action.admin;

import org.apache.log4j.Logger;

import com.jivesoftware.community.action.admin.AdminActionSupport;

public class JiveFooAdminAction extends AdminActionSupport {
	private static final long serialVersionUID = 2567147667705665199L;
	private static final Logger s_log = Logger.getLogger(JiveFooAdminAction.class);

	@Override
	public String input() {
		if (s_log.isDebugEnabled()) { s_log.debug("input called..."); }

		if (!isSystemAdmin()) {
			return UNAUTHORIZED;
		} // end if

		return INPUT;
	} // end input

	@Override
	public String execute() {
		if (s_log.isDebugEnabled()) { s_log.debug("execute called..."); }
		if (!isSystemAdmin()) {
			return UNAUTHORIZED;
		} // end if

		addActionMessage(getText("foo.action.message.success"));

		return SUCCESS;
	} // end execute

} // end class