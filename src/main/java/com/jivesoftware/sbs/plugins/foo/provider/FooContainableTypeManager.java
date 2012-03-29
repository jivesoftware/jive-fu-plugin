package com.jivesoftware.sbs.plugins.foo.provider;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.base.User;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveIterator;
import com.jivesoftware.community.impl.QueryCache;
import com.jivesoftware.community.objecttype.ContainableTypeManager;
import com.jivesoftware.community.util.collect.JiveIterators;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooManager;
import com.jivesoftware.sbs.plugins.foo.FooResultFilter;

public class FooContainableTypeManager implements ContainableTypeManager {
    private static final Logger log = Logger.getLogger(FooContainableTypeManager.class);
    private FooManager fooManager;

    @Override
    public void deleteAllContent(JiveContainer container) {
        fooManager.deleteFoos(container);
    }

    @Override
    public void deleteAllContent(User user) {
        fooManager.deleteFoos(user);
    }

    @Override
    public List<Long> getContentIDs(JiveContainer container, boolean recursive) {
        FooResultFilter filter = new FooResultFilter();
        filter.setContainer(container);
        filter.setRecursive(recursive);

        JiveIterator<Foo> foos = fooManager.getFoos(filter);
        return JiveIterators.<Foo, Long>mapToList((Iterable<Foo>) foos, new JiveIterators.Mapper<Foo, Long>() {
            public Long map(Foo foo) {
                return foo.getID();
            }
        });
    }

    @Override
    public int getContentCount(User user) {
        FooResultFilter filter = new FooResultFilter();
        filter.setUser(user);
        return fooManager.getFooCount(filter);
    }

    @Override
    public int getContentCount(JiveContainer container, boolean recursive) {
        FooResultFilter filter = new FooResultFilter();
        filter.setContainer(container);
        filter.setRecursive(recursive);
        return fooManager.getFooCount(filter);
    }

    @Override
    public void migrateAllContent(JiveContainer srcContainer, JiveContainer destContainer) {
        FooResultFilter filter = FooResultFilter.createDefaultFilter();
        filter.setContainer(srcContainer);

        int count = fooManager.getFooCount(filter);

        int blockSize = QueryCache.BLOCK_SIZE;
        int blocks = (int) Math.ceil(count / blockSize);

        filter.setNumResults(blockSize);

        log.debug("Migrating foos ...");
        for (int i = 0; i <= blocks; i++) {
            for (Foo foo : fooManager.getFoos(filter)) {
                log.debug("Moving brick oven id " + foo.getID() + ".");
                fooManager.moveFoo(foo, destContainer);
            }
        }
    }

    @Required
    public void setFooManager(FooManager fooManager) {
        this.fooManager = fooManager;
    }


}
