package com.jivesoftware.sbs.plugins.foo.community.places;

import java.util.HashMap;
import java.util.Map;

import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.web.component.AbstractActionLink;
import com.jivesoftware.sbs.plugins.foo.proxy.FooPermHelper;

public class PlaceCreateFooLink extends AbstractActionLink {

	public static final String JSPA_LINK = "/create-foo!input.jspa";

	@Override
	public boolean isVisible() {
		JiveContainer jiveContainer = getContainer();
		return FooPermHelper.getCanCreateFoo(jiveContainer);
	}

	@Override
	public String getUrl() {
		return JSPA_LINK;
	}

	@Override
	public Map<String, String> getUrlParams() {
		JiveContainer jiveContainer = getContainer();
		return getParamsMap(jiveContainer);
	}

	private JiveContainer getContainer() {
		return uiComponentContext.getContainer();
	}

	private Map<String, String> getParamsMap(JiveContainer jiveContainer) {
		Map<String, String> params = new HashMap<String, String>();

		if (jiveContainer != null) {
			params.put("container", String.valueOf(jiveContainer.getID()));
			params.put("containerType", String.valueOf(jiveContainer.getObjectType()));
		}

		return params;
	}

}
