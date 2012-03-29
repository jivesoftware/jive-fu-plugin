package com.jivesoftware.sbs.plugins.foo.provider;

import com.jivesoftware.base.User;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.aaa.authz.EntitlementTypeProvider.EntitlementType;
import com.jivesoftware.community.eae.objecttype.AbstractContentNotificationInfoProvider;
import com.jivesoftware.eae.constants.ActivityConstants.Type;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;

/**
 * Provides information on Foo-specific update email notifications
 */
public class FooNotificationInfoProvider extends AbstractContentNotificationInfoProvider {

    @Override
    public boolean canUserCreateNewContent(User user, JiveObject targetObject, JiveContainer container) {
        return getProvider().isUserEntitled(user, container, FooObjectType.FOO_TYPE_ID, EntitlementType.CREATE);
    }

    @Override
    public boolean canUserRespond(User user, JiveObject targetObject, JiveContainer container) {
        return getProvider().isUserEntitled(user, targetObject, EntitlementType.CREATE_COMMENT);
    }

    @Override
    public String getTemplateKey(JiveObject object, Type activityType) {
        return "watches.email.foo";
    }

}
