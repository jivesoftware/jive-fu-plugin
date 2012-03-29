package com.jivesoftware.sbs.plugins.foo.action;

import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.base.event.v2.EventDispatcher;
import com.jivesoftware.community.ContainerAwareEntityDescriptor;
import com.jivesoftware.community.ContentTag;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveIterator;
import com.jivesoftware.community.NotFoundException;
import com.jivesoftware.community.aaa.authz.EntitlementTypeProvider.EntitlementType;
import com.jivesoftware.community.objecttype.EntitlementCheckProvider;
import com.jivesoftware.community.renderer.impl.v2.TinyMCESupport;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooEvent;

public class ViewFooAction extends FooActionSupport {
    private JiveIterator<ContentTag> tags;
    private EventDispatcher eventDispatcher;
    private EntitlementCheckProvider<Foo> entitlementCheckProvider;

    @Override
    public String execute() {
        if(foo != null) {

            tags = tagManager.getTags(foo);
            if(eventDispatcher != null) {
                eventDispatcher.fire(new FooEvent(FooEvent.Type.VIEWED, new ContainerAwareEntityDescriptor(foo.getObjectType(), foo.getID(), foo.getJiveContainer().getID(), foo.getJiveContainer().getObjectType())));
            }

            return SUCCESS;
        }

        return ERROR;
    }

    public JiveIterator<ContentTag> getFooTags() {
        return tags;
    }

    @Override
    public JiveContainer getContainer() {
        if(foo == null) {
            return null;
        }

        try {
            return jiveObjectLoader.getJiveContainer(foo.getContainerType(), foo.getContainerID());
        } catch (NotFoundException e) {
            return super.getContainer();
        }
    }

    public boolean getCanEdit() {
        return entitlementCheckProvider.isUserEntitled(foo, EntitlementType.EDIT);
    }

    public String getMacroJavaScript() {
        return TinyMCESupport.getMacroJavaScript(getGlobalRenderManager());
    }

    @Required
    public void setJiveEventDispatcher(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Required
    public void setFooEntitlementCheckProvider(EntitlementCheckProvider<Foo> entitlementCheckProvider) {
        this.entitlementCheckProvider = entitlementCheckProvider;
    }

}

