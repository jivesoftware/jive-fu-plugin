package com.jivesoftware.sbs.plugins.foo.provider;

import org.apache.log4j.Logger;

import com.jivesoftware.base.User;
import com.jivesoftware.community.EntityDescriptor;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveContentObject;
import com.jivesoftware.community.UserContainer;
import com.jivesoftware.community.aaa.authz.EntitlementTypeProvider.EntitlementType;
import com.jivesoftware.community.aaa.authz.EntitlementTypeProvider.Type;
import com.jivesoftware.community.entitlements.Mask;
import com.jivesoftware.community.impl.BaseEntitlementProvider;
import com.jivesoftware.community.lifecycle.spring.BeanProvider;
import com.jivesoftware.community.util.BasePermHelper;
import com.jivesoftware.community.util.JiveContainerPermHelper;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;

public class FooEntitlementCheckProvider extends BaseEntitlementProvider<Foo> {
    private static final Logger log = Logger.getLogger(FooEntitlementCheckProvider.class);

    private BeanProvider<FooObjectType> fooObjectTypeProvider;

    @Override
    public boolean isUserEntitled(Foo foo, Type type) {
        return isUserEntitled(getEffectiveUser(), foo, type);
    }

    @Override
    public boolean isUserEntitled(JiveContainer container, Type type) {
        return isUserEntitled(getEffectiveUser(), container, type);
    }

    @Override
    public boolean isUserEntitled(User user, Foo foo, Type type) {
        if (isAnyNull(user, foo, type)) {
            return false;
        }

        if (EntitlementType.EDIT.equals(type)) {
            return ownerOperationAllowed(user, foo);
        }
        else if (EntitlementType.DELETE.equals(type)) {
            return ownerOperationAllowed(user, foo);
        }
        else if (EntitlementType.VIEW.equals(type)) {
            return handleView(user, foo);
        }
        else if (EntitlementType.CREATE_COMMENT.equals(type)) {
            return isUserEntitled(user, foo.getJiveContainer(), type);
        }
        else if (EntitlementType.RATE.equals(type)) {
            return handleRate(user, foo);
        }

        log.warn("ObservationEntitlementCheckProvider.isUserEntitled(user, post, type) unable to handle the type '"
                + type + "'");
        return false;
    }

    @Override
    public boolean isUserEntitled(User user, JiveContainer container, Type type) {
        if (isAnyNull(user, container, type) || isBannedFromPosting(user)) {
            return false;
        }

        Mask mask;
        if (EntitlementType.CREATE.equals(type)) {
            mask = getCreateMask();
        }
        else if (EntitlementType.ADMIN.equals(type)) {
            mask = getAdminMask();
        }
        else if (EntitlementType.VIEW.equals(type)) {
            mask = getViewMask();
        }
        else if (EntitlementType.CREATE_COMMENT.equals(type)) {
            mask = getCommentMask();
        }
        else if (EntitlementType.DELETE.equals(type)) {
            // CS-17140 - need to special handle this calling isAdmin because you cannot grant delete permissions
            // in the UI and they are needed to delete the Foo Container when deleting a space or project
            // TODO: implement "delete" entitlement
            return JiveContainerPermHelper.isContainerAdminOrModerator(container) || BasePermHelper.isSystemAdmin();
        }
            else {
            return false;
        }

        return isEntitled(user, container, mask);

    }

    protected boolean isEntitled(User user, Foo foo, Mask... masks) {
        JiveContainer container = foo.getJiveContainer();
        if (container instanceof UserContainer) {
            UserContainer uc = (UserContainer)container;
            return (user.getID() == uc.getUserID()|| super.isEntitled(user, new EntityDescriptor(foo), masks));
        }
        else {
            return isEntitled(user, getEntitledContainer(container, FooObjectType.FOO_TYPE_ID), FooObjectType.FOO_TYPE_ID, masks);
        }
    }

    protected boolean ownerOperationAllowed(User user, Foo foo) {
        if (isBannedFromPosting(user)) {
            return false;
        }

        boolean canCreateInContainer = isEntitled(user, foo.getJiveContainer(), getCreateMask());

        return (canCreateInContainer && isOwner(foo, user)) || isAdmin(foo, user) || isModerator(foo, user);
    }

    protected boolean handleView(User user, Foo foo) {
        // User must be the owner or a moderator if the foo is in a moderated state
        if (foo.getStatus() == JiveContentObject.Status.ABUSE_HIDDEN
                || foo.getStatus() == JiveContentObject.Status.AWAITING_MODERATION)
        {
            if (!(isOwner(foo, user) || isAdmin(foo, user) || isModerator(foo, user))) {
                return false;
            }
        }

        return isEntitled(user, foo.getJiveContainer(), getViewMask());

    }

    protected boolean handleRate(User user, Foo foo) {
        if (handleView(user, foo)) {
            if(foo.getJiveContainer() instanceof UserContainer) {
                return true;
            }

            return isEntitled(user, foo, getRateMask());
        }
        return false;
    }

    protected boolean isEntitled(User user, JiveContainer container, Mask mask) {
        int objectType = fooObjectTypeProvider.get().getID();
        if (super.isEntitled(user, getEntitledContainer(container, objectType), objectType, mask)) {
            return true;
        }

        return false;
    }

    protected boolean isOwner(Foo foo, User user) {
        if (user == null || user.isAnonymous()) {
            return false;
        }

        return foo.getUser().getID() == user.getID();
    }

    protected boolean isAdmin(Foo foo, User user) {
        return JiveContainerPermHelper.isContainerAdmin(getEntitledContainer(foo.getJiveContainer(), fooObjectTypeProvider.get().getID()), user);
    }

     protected boolean isAdmin(JiveContainer container, User user) {
        return JiveContainerPermHelper.isContainerAdmin(getEntitledContainer(container, fooObjectTypeProvider.get().getID()), user);
    }

    protected boolean isModerator(Foo foo, User user) {
        return JiveContainerPermHelper.isContainerModerator(getEntitledContainer(foo.getJiveContainer(), fooObjectTypeProvider.get().getID()), user);
    }

    public void setFooObjectTypeProvider(BeanProvider<FooObjectType> fooObjectTypeProvider) {
        this.fooObjectTypeProvider = fooObjectTypeProvider;
    }

}
