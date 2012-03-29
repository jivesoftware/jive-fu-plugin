package com.jivesoftware.sbs.plugins.foo.proxy;

import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.base.AuthToken;
import com.jivesoftware.base.UnauthorizedException;
import com.jivesoftware.base.User;
import com.jivesoftware.base.aaa.AuthenticationProvider;
import com.jivesoftware.base.proxy.ProxyUtils;
import com.jivesoftware.base.util.UserPermHelper;
import com.jivesoftware.community.Image;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveContainerManager;
import com.jivesoftware.community.JiveContentObject.Status;
import com.jivesoftware.community.JiveIterator;
import com.jivesoftware.community.NotFoundException;
import com.jivesoftware.community.ResultFilter;
import com.jivesoftware.community.aaa.authz.EntitlementTypeProvider;
import com.jivesoftware.community.aaa.authz.EntitlementTypeProvider.EntitlementType;
import com.jivesoftware.community.impl.ObjectFactory;
import com.jivesoftware.community.objecttype.EntitlementCheckProvider;
import com.jivesoftware.community.proxy.IteratorProxy;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooBean;
import com.jivesoftware.sbs.plugins.foo.FooManager;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;
import com.jivesoftware.sbs.plugins.foo.FooResultFilter;

public class FooManagerProxy implements FooManager, ObjectFactory<Foo>, IteratorProxy.ProxyFactory<Foo> {
    private FooManager delegate;
    private AuthenticationProvider authProvider;
    private JiveContainerManager containerManager;
    private EntitlementCheckProvider<Foo> entitlementCheckProvider;
    private EntitlementTypeProvider entitlementTypeProvider;

    @Override
    public void deleteFoo(Foo foo) {
        if (entitlementCheckProvider.isUserEntitled(foo, EntitlementType.DELETE)) {
            delegate.deleteFoo(foo);
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public void deleteFoos(JiveContainer container) {
        if (entitlementTypeProvider.isUserEntitled(container, FooObjectType.FOO_TYPE_ID, EntitlementType.DELETE)) {
            delegate.deleteFoos(container);
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public void deleteFoos(User user) {
        if (UserPermHelper.isUserAdmin(getEffectiveUser())) {
            delegate.deleteFoos(user);
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public Foo moveFoo(Foo foo, JiveContainer destination) {
        if(entitlementCheckProvider.isUserEntitled(foo, EntitlementType.DELETE) && entitlementCheckProvider.isUserEntitled(destination, EntitlementType.CREATE)) {
            return delegate.moveFoo(foo, destination);
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public Foo getFoo(long id) {
        Foo foo = delegate.getFoo(id);

        if(foo != null) {

            if (entitlementCheckProvider.isUserEntitled(foo, EntitlementType.VIEW)) {
                foo = createProxy(foo, authProvider.getAuthToken());
            } else {
                throw new UnauthorizedException();
            }
        }

        return foo;
    }

    @Override
    public Foo createProxy(Foo foo, AuthToken authToken) {
        if (entitlementCheckProvider.isUserEntitled(foo, EntitlementType.VIEW)) {
            return ProxyUtils.proxyObject(FooProxy.class, foo, authToken);
        }

        return null;
    }

    @Override
    public int getFooCount(FooResultFilter resultFilter) {
        return getFooCount(this.getFoos(resultFilter));
    }

    protected int getFooCount(JiveIterator<Foo> foos) {
        int count = 0;
        while (foos.hasNext()) {
            foos.next();
            count++;
        }

        return count;
    }

    @Override
    public JiveIterator<Foo> getFoos(FooResultFilter resultFilter) {
        int start = resultFilter.getStartIndex();
        int numResults = resultFilter.getNumResults();
        resultFilter.setStartIndex(0);
        resultFilter.setNumResults(ResultFilter.NULL_INT);

        // query core api
        JiveIterator<Foo> foos = delegate.getFoos(resultFilter);

        // reset filter
        resultFilter.setStartIndex(start);
        resultFilter.setNumResults(numResults);

        // set start/numResults to be valid entries for the iteration
        start = (start == ResultFilter.NULL_INT || start < 0) ? 0 : start;
        numResults = (numResults == ResultFilter.NULL_INT) ? -1 : numResults;

        // proxy results
        return new IteratorProxy<Foo>(foos, authProvider.getAuthToken(), start, numResults);
    }

    protected JiveIterator<Foo> createIteratorProxy(JiveIterator<Foo> foos) {
        return new IteratorProxy<Foo>(foos, authProvider.getAuthToken());
    }

    protected boolean isVisible(long fooId) {
        try {
            this.getFoo(fooId);
            return true;
        } catch (UnauthorizedException e) {
            return false;
        }
    }

    @Override
    public Foo saveFoo(FooBean bean, JiveIterator<Image> images) {
        try {
            JiveContainer container = containerManager.getJiveContainer(bean.getContainerType(), bean.getContainerID());
            if (entitlementCheckProvider.isUserEntitled(container, EntitlementType.CREATE)) {
                return delegate.saveFoo(bean, images);
            } else {
                throw new UnauthorizedException();
            }
        } catch (NotFoundException e) {
            throw new UnauthorizedException();
        }
    }

    @Override
    public Foo updateFoo(Foo foo, JiveIterator<Image> images) {
        if (entitlementCheckProvider.isUserEntitled(foo, EntitlementType.EDIT)) {
            return delegate.updateFoo(foo, images);
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public Foo updateFooStatus(Foo foo, Status status) {
        if (entitlementCheckProvider.isUserEntitled(foo, EntitlementType.EDIT)) {
            return delegate.updateFooStatus(foo, status);
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public Foo loadObject(long id) throws NotFoundException {
        return this.getFoo(id);
    }

    @Override
    public Foo loadObject(String id) throws NotFoundException {
        Foo foo = (Foo)((ObjectFactory)delegate).loadObject(id);
        if (entitlementCheckProvider.isUserEntitled(foo, EntitlementType.VIEW)) {
            return foo;
        } else {
            return null;
        }
    }

    protected User getEffectiveUser() {
        return authProvider.getAuthentication().getUser();
    }

    @Required
    public void setDelegate(FooManager delegate) {
        this.delegate = delegate;
    }

    @Required
    public void setAuthProvider(AuthenticationProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Required
    public void setContainerManager(JiveContainerManager containerManager) {
        this.containerManager = containerManager;
    }

    @Required
    public void setEntitlementTypeProvider(EntitlementTypeProvider entitlementTypeProvider) {
        this.entitlementTypeProvider = entitlementTypeProvider;
    }

    @Required
    public void setEntitlementCheckProvider(EntitlementCheckProvider<Foo> entitlementCheckProvider) {
        this.entitlementCheckProvider = entitlementCheckProvider;
    }
}
