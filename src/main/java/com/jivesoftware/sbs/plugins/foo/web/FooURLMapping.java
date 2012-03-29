package com.jivesoftware.sbs.plugins.foo.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.dispatcher.mapper.ActionMapping;

import com.jivesoftware.community.web.struts.converter.JiveObjectConverter;
import com.jivesoftware.community.web.struts.mapping.JiveObjectURLMapping;
import com.jivesoftware.community.web.struts.mapping.URLMapping;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;

public class FooURLMapping implements URLMapping, JiveObjectURLMapping {

    public static final String OBJECT_TYPE_PARAM = JiveObjectConverter.OBJECT_TYPE_PARAM;
    public static final String OBJECT_PARAM = "foo";

    @Override
    @SuppressWarnings("unchecked")
    public void process(String uri, ActionMapping mapping) {
        String[] elements = uri.split("/");
        Map<String, String> params = mapping.getParams();
        if (null == params) {
            params = new HashMap<String, String>();
        }

        if (elements.length > 3) {
            mapping.setName("edit-foo");
            mapping.setMethod("input");
            params.put("foo", elements[2]);
        } else if (elements.length > 2) {
            mapping.setName("view-foo");
            params.put(OBJECT_PARAM, elements[2]);
            params.put(OBJECT_TYPE_PARAM, Integer.toString(FooObjectType.FOO_TYPE_ID));
        } else {
            mapping.setName("content");
            params.put("filterID", "contentstatus[published]_objecttype-objecttype[" + FooObjectType.FOO_TYPE_ID + "]");
        }

        mapping.setNamespace("");
        mapping.setParams(params);
    }

    @Override
    public String getObjectTypeParamName() {
        return OBJECT_TYPE_PARAM;
    }

    @Override
    public String getObjectIdParamName() {
        return OBJECT_PARAM;
    }
}
