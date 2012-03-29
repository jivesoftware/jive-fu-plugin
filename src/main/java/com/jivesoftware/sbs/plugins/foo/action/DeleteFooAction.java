package com.jivesoftware.sbs.plugins.foo.action;

import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.community.action.util.AlwaysDisallowAnonymous;
import com.jivesoftware.community.action.util.Decorate;
import com.jivesoftware.community.web.JiveResourceResolver;
import com.jivesoftware.community.web.struts.SetReferer;
import com.jivesoftware.sbs.plugins.foo.FooManager;

@AlwaysDisallowAnonymous
@Decorate(false)
@SetReferer(false)
public class DeleteFooAction extends FooActionSupport {
    private FooManager fooManager;

    @Override
    public String execute() {
        fooManager.deleteFoo(foo);

        return SUCCESS;
    }

    @Required
    public void setFooManager(FooManager fooManager) {
        this.fooManager = fooManager;
    }

    public String getRedirectURL() {
        return JiveResourceResolver.getJiveObjectURL(foo.getJiveContainer(), false);
    }
}
