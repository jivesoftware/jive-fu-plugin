package com.jivesoftware.sbs.plugins.foo.provider;

import static com.jivesoftware.community.JiveContentObject.Status.ABUSE_HIDDEN;
import static com.jivesoftware.community.JiveContentObject.Status.ABUSE_VISIBLE;
import static com.jivesoftware.community.JiveContentObject.Status.AWAITING_MODERATION;

import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.community.JiveContentObject.Status;
import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.moderation.DefaultModerationStrategy;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooManager;

public class FooModerationStrategy extends DefaultModerationStrategy {

    private FooManager fooManager;

    @Override
    public void stateChangeCallback(JiveObject jiveObject, Status status) {
        Foo foo = fooManager.getFoo(jiveObject.getID());

        if((foo.getStatus() == ABUSE_HIDDEN || foo.getStatus() == ABUSE_VISIBLE) && status == AWAITING_MODERATION) {
            return;
        }

        fooManager.updateFooStatus(foo, status);
    }

    @Required
    public void setFooManager(FooManager fooManager) {
        this.fooManager = fooManager;
    }

}
