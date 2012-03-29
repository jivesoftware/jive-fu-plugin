package com.jivesoftware.sbs.plugins.foo.provider;

import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.community.ApprovalWorkflowView;
import com.jivesoftware.community.NotFoundException;
import com.jivesoftware.community.lifecycle.spring.BeanProvider;
import com.jivesoftware.community.moderation.ModerationUIProvider;
import com.jivesoftware.community.objecttype.JiveObjectFactory;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;

public class FooModerationUIProvider implements ModerationUIProvider {

    private BeanProvider<FooObjectType> fooObjectTypeProvider;
    private JiveObjectFactory<Foo> objectFactory;

    @Override
    public int getAdminOrder() {
        return fooObjectTypeProvider.get().getID();
    }

    @Override
    public String getModerationDisplayName() {
        return "admin.moderation.settings.content.foo";
    }

    @Override
    public ApprovalWorkflowView getModerationView(long id) {
        ApprovalWorkflowView view = null;
        try {
            view = new ApprovalWorkflowView(objectFactory.loadObject(id));
            view.setShowEditLink(true);
            view.setShowContextLink(true);
        } catch (NotFoundException e) {

        }

        return view;
    }

    @Override
    public String getRootAdminString() {
        return "admin.moderation.settings.applied.root";
    }

    @Override
    public String getSubContainerAdminString() {
        return " admin.moderation.settings.applied.foos";
    }

    @Override
    public boolean isAdminSetting() {
        return true;
    }

    @Override
    public boolean isGlobalOnly() {
        return false;
    }

    @Required
    public void setFooObjectTypeProvider(BeanProvider<FooObjectType> fooObjectTypeProvider) {
        this.fooObjectTypeProvider = fooObjectTypeProvider;
    }

    @Required
    public void setObjectFactory(JiveObjectFactory<Foo> objectFactory) {
        this.objectFactory = objectFactory;
    }

}
