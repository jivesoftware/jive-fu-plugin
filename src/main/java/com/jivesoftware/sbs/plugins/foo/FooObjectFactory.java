package com.jivesoftware.sbs.plugins.foo;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.base.AuthToken;
import com.jivesoftware.base.UnauthorizedException;
import com.jivesoftware.community.NotFoundException;
import com.jivesoftware.community.impl.BlockObjectFactory;
import com.jivesoftware.community.impl.ObjectFactory;
import com.jivesoftware.community.objecttype.JiveObjectFactory;
import com.jivesoftware.community.proxy.IteratorProxy;

public class FooObjectFactory implements JiveObjectFactory<Foo>, BlockObjectFactory<Foo> {
    private static final Logger log = Logger.getLogger(FooObjectFactory.class);

    private ObjectFactory<Foo> objectFactory;
    private ObjectFactory<Foo> proxiedObjectFactory;
    private IteratorProxy.ProxyFactory<Foo> proxyFactory;


    @Override
    public Foo createProxy(Foo foo, AuthToken authToken) {
        return proxyFactory.createProxy(foo, authToken);
    }

    @Override
    public Foo loadObject(long id) throws NotFoundException {
        return objectFactory.loadObject(id);
    }

    @Override
    public Foo loadObject(String id) throws NotFoundException {
        return objectFactory.loadObject(id);
    }

    @Override
    public Foo loadProxyObject(long id) throws NotFoundException, UnauthorizedException {
        return proxiedObjectFactory.loadObject(id);
    }

    @Override
    public Foo loadProxyObject(String id) throws NotFoundException, UnauthorizedException {
        return proxiedObjectFactory.loadObject(id);
    }

    @Override
    public List<Foo> loadObjects(List<Long> ids) {
        if (objectFactory instanceof BlockObjectFactory) {
            return ((BlockObjectFactory<Foo>) objectFactory).loadObjects(ids);
        }

        List<Foo> foos = new ArrayList<Foo>(ids.size());
        for (long id : ids) {
            try {
                foos.add(loadObject(id));
            } catch (NotFoundException e) {
                log.error(e.getMessage(), e);
            }
        }

        return foos;
    }

    @Required
    public void setObjectFactory(ObjectFactory<Foo> objectFactory) {
        this.objectFactory = objectFactory;
    }

    @Required
    public void setProxiedObjectFactory(ObjectFactory<Foo> proxiedObjectFactory) {
        this.proxiedObjectFactory = proxiedObjectFactory;
    }

    @Required
    public void setProxyFactory(IteratorProxy.ProxyFactory<Foo> proxyFactory) {
        this.proxyFactory = proxyFactory;
    }


}
