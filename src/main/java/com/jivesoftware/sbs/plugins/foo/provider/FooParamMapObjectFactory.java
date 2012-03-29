package com.jivesoftware.sbs.plugins.foo.provider;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.community.JiveContext;
import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.NotFoundException;
import com.jivesoftware.community.ParamMapObjectFactory;
import com.jivesoftware.sbs.plugins.foo.FooManager;

public class FooParamMapObjectFactory implements ParamMapObjectFactory {

    private FooManager fooManager;

    @Override
    public JiveObject loadObject(JiveContext jiveContext, Map<String, Object> params) throws NotFoundException {
        String fooID = (String) params.get("foo");
        if(fooID == null) {
            return null;
        }

        try {
            return fooManager.getFoo(Long.valueOf(fooID));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Required
    public void setFooManager(FooManager fooManager) {
        this.fooManager = fooManager;
    }


}
