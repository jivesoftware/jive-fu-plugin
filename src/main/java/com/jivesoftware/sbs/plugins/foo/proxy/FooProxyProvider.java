package com.jivesoftware.sbs.plugins.foo.proxy;

import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.base.AuthToken;
import com.jivesoftware.base.proxy.JiveProxy;
import com.jivesoftware.community.objecttype.EntitlementCheckProvider;
import com.jivesoftware.community.proxy.JiveProxyProvider;
import com.jivesoftware.sbs.plugins.foo.Foo;

public class FooProxyProvider implements JiveProxyProvider<Foo> {

    private EntitlementCheckProvider<Foo> entitlementCheckProvider;

    @Override
    public Foo createProxy(Foo object, AuthToken authToken) {
        FooProxy fooProxy = new FooProxy();
        fooProxy.init(object, authToken);
        fooProxy.setEntitlementCheckProvider(entitlementCheckProvider);

        return fooProxy;
    }

    @Override
    public Class<? extends JiveProxy<Foo>> getSupportedProxyClass() {
        return FooProxy.class;
    }

    @Required
    public void setEntitlementCheckProvider(EntitlementCheckProvider<Foo> entitlementCheckProvider) {
        this.entitlementCheckProvider = entitlementCheckProvider;
    }

}
