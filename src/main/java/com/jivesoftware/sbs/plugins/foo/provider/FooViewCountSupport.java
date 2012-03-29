package com.jivesoftware.sbs.plugins.foo.provider;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Maps;
import com.jivesoftware.base.event.v2.EventDispatcher;
import com.jivesoftware.cache.Cache;
import com.jivesoftware.community.ContainerAwareEntityDescriptor;
import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.objecttype.ViewCountSupport;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooEvent;
import com.jivesoftware.sbs.plugins.foo.action.FooActionSupport;
import com.opensymphony.xwork2.ActionInvocation;

public class FooViewCountSupport implements ViewCountSupport {

    private Cache<Long, Integer> countCache;
    private EventDispatcher eventDispatcher;

    @Override
    public Cache<Long, Integer> getCountCache() {
        return countCache;
    }

    @Override
    public String getCacheLockKey() {
        return "fooViewCount";
    }

    @Override
    public Foo getObjectFromActionInvocation(ActionInvocation actionInvocation) {
        return ((FooActionSupport) actionInvocation.getAction()).getFoo();
    }

    @Required
    public void setCountCache(Cache<Long, Integer> fooCountCache) {
        this.countCache = fooCountCache;
    }

    @Required
    public void setEventDispatcher(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void fireEventsAfterView(JiveObject jiveObject) {
        Foo foo = (Foo) jiveObject;
        eventDispatcher.fire(new FooEvent(FooEvent.Type.VIEWED, new ContainerAwareEntityDescriptor(foo), Maps.<String, Object>newHashMap(), foo.getStatus()));
    }

}
