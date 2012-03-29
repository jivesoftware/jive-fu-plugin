package com.jivesoftware.sbs.plugins.foo.action;

import com.jivesoftware.community.EntityDescriptor;
import com.jivesoftware.community.action.ContentActionSupport;
import com.jivesoftware.sbs.plugins.foo.Foo;

public class FooActionSupport extends ContentActionSupport {
	protected Foo foo;

	public Foo getFoo() {
		return foo;
	}

	public void setFoo(Foo foo) {
		this.foo = foo;
	}

    public boolean isFollowed() {
        return followingManager.isFollowed(getUser(), new EntityDescriptor(foo));
    }
}
