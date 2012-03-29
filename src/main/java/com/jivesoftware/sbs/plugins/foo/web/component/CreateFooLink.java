package com.jivesoftware.sbs.plugins.foo.web.component;

import java.util.HashMap;
import java.util.Map;

import com.jivesoftware.community.JiveConstants;
import com.jivesoftware.community.web.component.AbstractCreateContentLink;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;

/**
 * Create Foo menu item displayed on global Create menu
 *
 * @author ryan
 *
 */
public class CreateFooLink extends AbstractCreateContentLink {

    @Override
    public boolean isVisible() {
        return isUserLoggedIn() && isValidContainerSelected();
    }

    protected boolean isUserLoggedIn() {
        return !uiComponentContext.getUser().isAnonymous();
    }

    protected boolean isValidContainerSelected() {
        return !(uiComponentContext.getUser().isAnonymous() && (uiComponentContext.getContainer() == null || uiComponentContext.getContainer().getID() == JiveConstants.ROOT_COMMUNITY_ID
                && uiComponentContext.getContainer().getObjectType() == JiveConstants.COMMUNITY));
    }

    @Override
    public Map<String, String> getUrlParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("contentType", String.valueOf(FooObjectType.FOO_TYPE_ID));
        return params;
    }
}
