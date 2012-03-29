package com.jivesoftware.sbs.plugins.foo.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.community.EntityDescriptor;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveContentObject;
import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.impl.search.provider.BaseIndexInfoProvider;
import com.jivesoftware.community.lifecycle.spring.BeanProvider;
import com.jivesoftware.community.search.IndexField;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooManager;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;

public class FooIndexInfoProvider extends BaseIndexInfoProvider {

    private static final String COUNT_SQL = "SELECT count(fooID) FROM jiveFoo WHERE "
        + "status = " + JiveContentObject.Status.PUBLISHED.intValue() + " AND "
        + "modificationDate <= ?";

    private static final String ID_SQL = "SELECT fooID FROM jiveFoo WHERE "
        + "status = " + JiveContentObject.Status.PUBLISHED.intValue() + " AND "
        + "fooID >= ? AND fooID <= ? AND modificationDate >= ? AND modificationDate <= ?";

    private static final String MAX_ID_SQL = "SELECT MAX(fooID) from jiveFoo WHERE "
        + "status = " + JiveContentObject.Status.PUBLISHED.intValue() + " AND "
        + "modificationDate <= ?";

    private static final String MIN_ID_SQL = "SELECT MIN(fooID) from jiveFoo WHERE "
        + "status = " + JiveContentObject.Status.PUBLISHED.intValue() + " AND "
        + "modificationDate <= ?";

    private BeanProvider<FooObjectType> fooObjectTypeProvider;
    private FooManager fooManager;

    @Override
    protected String getCountSQL() {
        return COUNT_SQL;
    }

    @Override
    protected String getIDsSQL() {
        return ID_SQL;
    }

    @Override
    protected Map<IndexField, String> getIndexFields(long id, boolean b) {
        Foo foo = fooManager.getFoo(id);
        final Map<IndexField, String> map = new HashMap<IndexField, String>();

        if(foo != null) {
            String tags = tagManager.getTagsAsString(foo);
            JiveContainer container = foo.getJiveContainer();
            List<EntityDescriptor> ids = util.getParentContainerIDs(container);

            map.put(IndexField.subject, foo.getSubject());
            map.put(IndexField.tags, tags);
            map.put(IndexField.objectID, String.valueOf(foo.getID()));
            map.put(IndexField.objectType, String.valueOf(foo.getObjectType()));
            map.put(IndexField.containerID, String.valueOf(container.getID()));
            map.put(IndexField.containerType, String.valueOf(container.getObjectType()));
            map.put(IndexField.creationDate, String.valueOf(foo.getCreationDate().getTime()));
            map.put(IndexField.modificationDate, String.valueOf(foo.getModificationDate().getTime()));
            map.put(IndexField.containerIDs, util.buildIDString(ids.iterator()));
            map.put(IndexField.userID, String.valueOf(foo.getUser().getID()));
            map.put(IndexField.author, util.buildIDString(foo.getAuthors()));
        }

        return map;
    }

    @Override
    protected String getLanguage(long id) {
        return null;
    }

    @Override
    protected String getMaxIDSQL() {
        return MAX_ID_SQL;
    }

    @Override
    protected String getMinIDSQL() {
        return MIN_ID_SQL;
    }

    @Override
    protected int getObjectTypeID() {
        return fooObjectTypeProvider.get().getID();
    }

    @Override
    public boolean isIndexable(JiveObject jiveObject) {
        Foo foo = null;
        if(jiveObject instanceof EntityDescriptor) {
            EntityDescriptor ed = (EntityDescriptor) jiveObject;
            if(ed.getObjectType() == getObjectTypeID()) {
                foo = fooManager.getFoo(ed.getID());
            }
        } else {
            foo = (Foo)jiveObject;
        }

        return foo == null ? false : foo.getStatus().isVisible();
    }

    @Required
    public void setFooObjectTypeProvider(BeanProvider<FooObjectType> fooObjectTypeProvider) {
        this.fooObjectTypeProvider = fooObjectTypeProvider;
    }

    @Required
    public void setFooManager(FooManager fooManager) {
        this.fooManager = fooManager;
    }

}
