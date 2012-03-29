package com.jivesoftware.sbs.plugins.foo.provider;

import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.base.User;
import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.TaggableTypeInfoProvider;
import com.jivesoftware.community.lifecycle.spring.BeanProvider;
import com.jivesoftware.community.objecttype.ContainableType;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;

public class FooTaggableTypeInfoProvider implements TaggableTypeInfoProvider {

    private BeanProvider<FooObjectType> fooObjectTypeProvider;

    @Override
    public ContainableType getContainableType() {
        return fooObjectTypeProvider.get();
    }

    @Override
    public boolean isAllowedToTag(JiveObject object, User user) {
        return true;
    }

    @Required
    public void setFooObjectTypeProvider(BeanProvider<FooObjectType> fooObjectTypeProvider) {
        this.fooObjectTypeProvider = fooObjectTypeProvider;
    }

}
