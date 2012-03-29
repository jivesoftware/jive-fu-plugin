package com.jivesoftware.sbs.plugins.foo.provider;

import com.jivesoftware.base.event.v2.BaseJiveEvent;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.tags.type.impl.ContentGenericTaggableTypeInfoProvider;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooEvent;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;
import com.jivesoftware.sbs.plugins.foo.proxy.FooPermHelper;

public class FooGenericTaggableTypeInfoProvider extends ContentGenericTaggableTypeInfoProvider {

    private static final String SELECT_SQL =
            "SELECT containerType, containerID, tagID FROM "
                    + "(SELECT containerType, containerID, tagID FROM jiveObjectTag, jiveFoo"
                    + " WHERE jiveObjectTag.objectType = " + FooObjectType.FOO_TYPE_ID
                    + " AND jiveObjectTag.objectID = jiveFoo.fooID"
                    + " AND (jiveFoo.status = " + Foo.Status.PUBLISHED.intValue()
                    + " OR jiveFoo.status = " + Foo.Status.ABUSE_VISIBLE.intValue() + ")"
                    + " UNION ALL"
                    + " SELECT containerType, containerID, tagID FROM jiveCommunityTag, jiveFoo"
                    + " WHERE jiveCommunityTag.objectType = " + FooObjectType.FOO_TYPE_ID
                    + " AND jiveCommunityTag.objectID = jiveFoo.fooID"
                    + " AND (jiveFoo.status = " + Foo.Status.PUBLISHED.intValue()
                    + " OR jiveFoo.status = " + Foo.Status.ABUSE_VISIBLE.intValue() + ")"
                    + ") temp"
                    + " ORDER BY containerType, containerID";

    @Override
    public EventType getMappedEventType(BaseJiveEvent e) {
        if (!(e instanceof FooEvent)) {
            return null;
        }
        FooEvent foo = (FooEvent) e;
        switch (foo.getType()) {
            case DELETED:
                return EventType.deleted;
        }
        return null;
    }

    @Override
    public boolean isTaggable(JiveContainer container) {
        return FooPermHelper.areFoosEnabled(container);
    }

    @Override
    public boolean isVisible(JiveObject jiveObject) {
        if (jiveObject instanceof Foo) {
        	Foo foo = (Foo) jiveObject;
            return foo.getStatus() == Foo.Status.PUBLISHED || foo.getStatus() == Foo.Status.ABUSE_VISIBLE;
        }

        return false;
    }

    @Override
    public String getTagCloudSummaryQuery() {
        return SELECT_SQL;
    }

}
