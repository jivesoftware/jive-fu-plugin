package com.jivesoftware.sbs.plugins.foo.impl;

import com.jivesoftware.community.stats.ViewCountManager;
import com.jivesoftware.sbs.plugins.foo.Foo;

public class ViewCountProvider {
    private ViewCountManager viewCountManager;

    public void setViewCountManager(ViewCountManager viewCountManager) {
        this.viewCountManager = viewCountManager;
    }

    public int getViewCount(Foo foo) {
        return viewCountManager.getViewCount(foo);
    }
}
