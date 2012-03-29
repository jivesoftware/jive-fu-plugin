package com.jivesoftware.sbs.plugins.foo.proxy;

import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.aaa.authz.EntitlementTypeProvider.EntitlementType;
import com.jivesoftware.community.lifecycle.JiveApplication;
import com.jivesoftware.community.objecttype.ContainableType;
import com.jivesoftware.community.objecttype.EntitlementCheckProvider;
import com.jivesoftware.community.util.BasePermHelper;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;

public class FooPermHelper extends BasePermHelper {
    public static boolean getCanCreateFoo(JiveContainer container) {
    	if (areFoosEnabled(container)) {
        	return getEntitlementCheckProvider().isUserEntitled(container, EntitlementType.CREATE);
        }

        return false;
    }

    public static boolean getCanViewFoos(JiveContainer container) {
        if (areFoosEnabled(container)) {
        	return getEntitlementCheckProvider().isUserEntitled(container, EntitlementType.VIEW);
        }

        return false;
    }

    public static boolean getCanDeleteFoo(Foo foo) {
        return getEntitlementCheckProvider().isUserEntitled(foo, EntitlementType.DELETE);
    }

    public static boolean getCanUpdateFoo(Foo foo) {
        return getEntitlementCheckProvider().isUserEntitled(foo, EntitlementType.EDIT);
    }

    public static boolean areFoosEnabled(JiveContainer container) {
        for (ContainableType type : container.getContentTypes()) {
            if (type.getID() == new FooObjectType().getID()) {
                return true;
            }
        }

        return false;
    }

    private static EntitlementCheckProvider<Foo> getEntitlementCheckProvider() {
        return (EntitlementCheckProvider<Foo>)JiveApplication.getContext().getSpringBean("fooEntitlementCheckProvider");
    }
}
