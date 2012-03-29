package com.jivesoftware.sbs.plugins.foo.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jivesoftware.community.web.soy.SoyModelDriven;

public class JiveSoyAction extends JiveFooActionSupport implements SoyModelDriven {
	private static final long serialVersionUID = 5443023657218107479L;
	private static final Logger s_log = Logger.getLogger(JiveSoyAction.class);

    @Override
    public String execute() {
    	if (s_log.isDebugEnabled()) { s_log.debug("execute called..."); }
        return SUCCESS;
    } // end execute

	@Override
	public Object getModel() {
    	if (s_log.isDebugEnabled()) { s_log.debug("getModel called..."); }
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("foo","bar");
		return params;
	} // end getModel

} // end class