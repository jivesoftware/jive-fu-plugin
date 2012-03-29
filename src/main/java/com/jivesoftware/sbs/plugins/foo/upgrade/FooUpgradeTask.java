package com.jivesoftware.sbs.plugins.foo.upgrade;

import org.apache.log4j.Logger;

import com.jivesoftware.community.upgrade.UpgradeTask;

public class FooUpgradeTask implements UpgradeTask {
	private static final Logger s_log = Logger.getLogger(FooUpgradeTask.class);
	    private UpgradeDao upgradeDao = new UpgradeDao();

	    public void doTask() throws Exception {
	    	if (s_log.isDebugEnabled()) { s_log.debug("Starting FooUpgradeTask..."); }
	    	upgradeDao.applySchema("FooUpgradeTask.xml");
	    	if (s_log.isDebugEnabled()) { s_log.debug("Completed!"); }
	    } // end doTask

	    public String getDescription() {	return "This task purges any bogus quickLink properties that were inadvertently saved prior to this version.";	}
	    public String getEstimatedRunTime() {	return "Less than one minute";	}
	    public String getInstructions() {	return ""; }
	    public String getName() {	return "Clear Empty QuickLinks";	}
	    public boolean isBackgroundTask() {	return false;	}

} // end class
