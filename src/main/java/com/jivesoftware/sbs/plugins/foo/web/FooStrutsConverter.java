package com.jivesoftware.sbs.plugins.foo.web;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.base.UnauthorizedException;
import com.jivesoftware.community.action.JiveActionSupport;
import com.jivesoftware.community.web.struts.JiveConversionErrorInterceptor;
import com.jivesoftware.community.web.struts.converter.JiveObjectTypeConverter;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooManager;

public class FooStrutsConverter extends JiveObjectTypeConverter {
    private static final Logger log = Logger.getLogger(FooStrutsConverter.class);
    private FooManager fooManager;

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
    try {
        long fooID = NumberUtils.toLong(values[0], -1);
        if (fooID > 0) {
        Foo foo = fooManager.getFoo(fooID);

        if (foo == null) {
            log.error("Could not convert to Foo for parameter: " + values[0], new Exception());
            context.put(JiveConversionErrorInterceptor.JIVE_CONVERSION_ERROR_KEY, JiveActionSupport.NOTFOUND);
        } else {
            return foo;
        }
        }
    } catch (UnauthorizedException e) {
        log.error("Could not convert to Foo for parameter: " + values[0], e);
        this.determineAuthError(context);
    } catch (Exception e) {
        log.error("Could not convert to Foo for parameter: " + values[0], e);
        setError(context, JiveActionSupport.ERROR);
    }

    return null;
    }

    @Override
    public String convertToString(Map context, Object o) {
        return String.valueOf(((Foo) o).getID());
    }

    @Required
    public void setFooManager(FooManager fooManager) {
        this.fooManager = fooManager;
    }

}
