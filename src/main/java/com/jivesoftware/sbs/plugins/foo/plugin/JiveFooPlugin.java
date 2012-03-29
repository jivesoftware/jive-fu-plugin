package com.jivesoftware.sbs.plugins.foo.plugin;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.base.event.v2.EventListenerRegistry;
import com.jivesoftware.base.plugin.Plugin;
import com.jivesoftware.base.proxy.ProxyUtils;
import com.jivesoftware.community.renderer.RenderManager;
import com.jivesoftware.sbs.plugins.foo.filter.FooFilter;
import com.jivesoftware.sbs.plugins.foo.listener.FooListener;
import com.jivesoftware.sbs.plugins.foo.proxy.FooProxyProvider;

public class JiveFooPlugin implements Plugin {
	private static final Logger s_log = Logger.getLogger(JiveFooPlugin.class);

    private static boolean PLUGIN_LOADED = false;

    private FooProxyProvider fooProxyProvider = null;
	private RenderManager m_renderManager = null;
	private FooFilter fooFilter = null;
	private FooListener fooListener = null;
	private EventListenerRegistry eventListenerRegistry = null;

	@Required
    public void setFooProxyProvider(FooProxyProvider fooProxyProvider) {
        this.fooProxyProvider = fooProxyProvider;
    } // end setFooProxyProvider - SPRING MANAGED

	public void setRenderManager(RenderManager renderManager) {
		m_renderManager = renderManager;
	} // end setRenderManager - SPRING MANAGED

	public void setEventListenerRegistry(EventListenerRegistry eventListenerRegistry) {
		this.eventListenerRegistry = eventListenerRegistry;
	} // end setEventListenerRegistry - SPRING MANAGED

	public void setFooFilter(FooFilter fooFilter) {
		this.fooFilter = fooFilter;
	} // end setFooFilter - SPRING MANAGED

	public void setFooListener(FooListener fooListener) {
		this.fooListener = fooListener;
	} // end setFooListener - SPRING MANAGED

    public void init() {
    	if (s_log.isDebugEnabled()) { s_log.debug("init called..."); }
    } // end destroy

    @Override
    public void initPlugin() {
        if (s_log.isDebugEnabled()) { s_log.debug("initPlugin called..."); }

        ProxyUtils.addProxyProvider(fooProxyProvider);

        eventListenerRegistry.register(fooListener);

        //TODO:  IS THIS SYNCHRONIZATION STILL NEEDED?
		synchronized (JiveFooPlugin.class) {
			if (!PLUGIN_LOADED) {
				m_renderManager.addRenderPlugin(fooFilter);
				PLUGIN_LOADED = true;
				if (s_log.isInfoEnabled()) { s_log.info("Registered ["+fooFilter.getName()+"]"); }
			} else {
				if (s_log.isInfoEnabled()) { s_log.info("Filter ["+fooFilter.getName()+"], should already be loaded!"); }
			} // end if
		} // end synchronized
    } // end initPlugin

    @Override
    public void destroy() {
    	if (s_log.isDebugEnabled()) { s_log.debug("destroy called..."); }

    	eventListenerRegistry.unregister(fooListener);

    } // end destroy

} // end class
