package com.jivesoftware.sbs.plugins.foo.provider;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.base.UserManager;
import com.jivesoftware.base.UserNotFoundException;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.aaa.authz.EntitlementTypeProvider.EntitlementType;
import com.jivesoftware.community.lifecycle.spring.BeanProvider;
import com.jivesoftware.community.objecttype.ContainableType;
import com.jivesoftware.community.objecttype.ContentObjectTypeInfoProvider;
import com.jivesoftware.community.objecttype.EntitlementCheckProvider;
import com.jivesoftware.community.objecttype.UserProfileInfoProvider;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooManager;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;
import com.jivesoftware.sbs.plugins.foo.FooResultFilter;

public class FooContentObjectTypeInfoProvider implements ContentObjectTypeInfoProvider {
    private static final Logger log = Logger.getLogger(FooContentObjectTypeInfoProvider.class);

    private FooManager fooManager;
    private UserManager userManager;

    private BeanProvider<FooObjectType> fooObjectTypeProvider;
    private EntitlementCheckProvider<Foo> entitlementCheckProvider;

    @Override
    public ContainableType getContainableType() {
        return fooObjectTypeProvider.get();
    }

    @Override
    public String getCreateNewFormRelativeURL(JiveContainer targetContainer, boolean isUpload, String tempObjectId, String tags, String subject) {
        StringBuilder url = new StringBuilder();
        url.append("create-foo!input.jspa?");
        url.append("container=").append(targetContainer.getID());
        url.append("&containerType=").append(targetContainer.getObjectType());
        return url.toString();
    }

    @Override
    public long getUserContentCount(long userID) {
        try {
            FooResultFilter filter = FooResultFilter.createDefaultFilter();
            filter.setUser(userManager.getUser(userID));
            return fooManager.getFooCount(filter);
        } catch (UserNotFoundException e) {
            log.error("Could not find user " + userID + " in order to obtain foo count", e);
            return 0;
        }
    }

    @Override
    public UserProfileInfoProvider getUserProfileInfoProvider() {
        return null;
    }

    @Override
    public boolean isBinaryBodyUploadCapable() {
        return false;
    }

    @Override
    public boolean userHasCreatePermsFor(JiveContainer container) {
        return entitlementCheckProvider.isUserEntitled(container, EntitlementType.CREATE);
    }

    @Required
    public void setEntitlementCheckProvider(EntitlementCheckProvider<Foo> entitlementCheckProvider) {
        this.entitlementCheckProvider = entitlementCheckProvider;
    }

    @Required
    public void setFooObjectTypeProvider(BeanProvider<FooObjectType> fooObjectTypeProvider) {
        this.fooObjectTypeProvider = fooObjectTypeProvider;
    }

    @Required
    public void setFooManager(FooManager eventManager) {
        this.fooManager = fooManager;
    }

    @Required
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
