package com.jivesoftware.sbs.plugins.foo.dao;

import java.util.List;

import com.jivesoftware.base.User;
import com.jivesoftware.community.EntityDescriptor;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.impl.CachedPreparedStatement;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooBean;
import com.jivesoftware.sbs.plugins.foo.FooResultFilter;

public interface FooDao {
    public FooBean getFoo(long id);

    public long saveFoo(FooBean object);

    public void updateFoo(FooBean object);

    public void deleteFoo(long id);

    public void moveFoo(Foo foo, JiveContainer destination);

    public CachedPreparedStatement getFooListSQL(FooResultFilter resultFilter);

    public CachedPreparedStatement getFooListCountSQL(FooResultFilter resultFilter);

    public List<Long> getAllFooIDs(JiveContainer container);

    public List<Long> getAllFooIDs(User user);

    public void deleteFoos(List<Long> fooIDs);

    public List<EntityDescriptor> getFrequentFooContainers(long userID);
}
