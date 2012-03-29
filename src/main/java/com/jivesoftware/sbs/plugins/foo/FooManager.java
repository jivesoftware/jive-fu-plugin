package com.jivesoftware.sbs.plugins.foo;

import com.jivesoftware.base.User;
import com.jivesoftware.community.Image;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveContentObject.Status;
import com.jivesoftware.community.JiveIterator;

public interface FooManager {

    public int getFooCount(FooResultFilter resultFilter);

    public JiveIterator<Foo> getFoos(FooResultFilter resultFilter);

    public Foo saveFoo(FooBean fooBean, JiveIterator<Image> images);

    public Foo updateFoo(Foo foo, JiveIterator<Image> images);

    public Foo updateFooStatus(Foo foo, Status status);

    public Foo getFoo(long id);

    public void deleteFoo(Foo foo);

    public void deleteFoos(JiveContainer container);

    public void deleteFoos(User user);

    public Foo moveFoo(Foo foo, JiveContainer target);
}
