package com.jivesoftware.sbs.plugins.foo.provider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.jivesoftware.base.UserTemplate;
import com.jivesoftware.base.database.ConnectionManager;
import com.jivesoftware.base.wiki.WikiContentHelper;
import com.jivesoftware.community.JiveGlobals;
import com.jivesoftware.community.eae.upgrade.provider.BaseActivityUpgradeProvider;
import com.jivesoftware.community.impl.ListJiveIterator;
import com.jivesoftware.community.renderer.impl.v2.JAXPUtils;
import com.jivesoftware.eae.api.Activity;
import com.jivesoftware.eae.api.ActivityID;
import com.jivesoftware.eae.api.EAEEntityDescriptor;
import com.jivesoftware.eae.api.User;
import com.jivesoftware.eae.constants.ActivityConstants;
import com.jivesoftware.eae.uuid.UUUID;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;
import com.jivesoftware.util.SqlUtils;
import com.jivesoftware.util.StringUtils;

/**
 * Facilitates the population of existing Foos data into the Enterprise
 * Activity Engine
 *
 */
public class FooActivityUpgradeProvider extends BaseActivityUpgradeProvider {

    private static final String COUNT = "SELECT count(*) FROM jiveFoo WHERE status = " + Foo.Status.PUBLISHED.intValue() + " AND creationDate >= ?";
    private static final String MIN = "SELECT MIN(fooID) FROM jiveFoo WHERE status = " + Foo.Status.PUBLISHED.intValue() + " AND creationDate >= ?";
    private static final String MAX = "SELECT MAX(fooID) FROM jiveFoo WHERE status = " + Foo.Status.PUBLISHED.intValue() + " AND creationDate >= ?";
    private static final String CONTENT = "" + "SELECT fooID, description, userID, containerType, containerID, creationDate " + "FROM jiveFoo " + "WHERE status = " + Foo.Status.PUBLISHED.intValue() + " AND fooID > ? AND creationDate >= ?"
            + " ORDER BY fooID ASC";

    private final RowMapper<Activity> mapper = new RowMapper<Activity>() {
        @Override
        public Activity mapRow(ResultSet rs, int rowNum) throws SQLException {
            Activity activity = new Activity();
            activity.setAID(new ActivityID(UUUID.createTimeUUID().toBytes()));

            EAEEntityDescriptor actDescriptor = new EAEEntityDescriptor();
            actDescriptor.setID(rs.getLong("fooID"));
            actDescriptor.setType(FooObjectType.FOO_TYPE_ID);
            activity.setDescriptor(actDescriptor);

            EAEEntityDescriptor context = new EAEEntityDescriptor();
            context.setID(rs.getLong("containerID"));
            context.setType(rs.getInt("containerType"));
            activity.setContext(context);

            EAEEntityDescriptor parent = new EAEEntityDescriptor();
            parent.setID(rs.getLong("fooID"));
            parent.setType(FooObjectType.FOO_TYPE_ID);
            activity.setParent(parent);

            String body = rs.getString("description");
            if (StringUtils.isEmpty(body)) {
                body = JAXPUtils.toXmlString(WikiContentHelper.newEmptyDocument());
            } else if (!WikiContentHelper.isKnownContent(body)) {
                body = JAXPUtils.toXmlString(WikiContentHelper.unknownContentToJiveDoc(body));
            }

            activity.setContent(StringUtils.toPlainText(body));
            activity.setSlug(StringUtils.chop(StringUtils.toPlainText(body), 250));
            activity.setSystemID(JiveGlobals.getJiveProperty(JiveGlobals.JIVE_INSTANCE_ID));
            activity.setTime(rs.getLong("creationDate"));
            activity.setType(ActivityConstants.Type.created.name());

            User user = new User(String.valueOf(rs.getLong("userID")));
            activity.setUID(user);

            List<com.jivesoftware.base.User> collaborators = new ArrayList<com.jivesoftware.base.User>();
            collaborators.add(new UserTemplate(rs.getLong("userID")));
            activity = addAuthorsToActivity(activity, new ListJiveIterator<com.jivesoftware.base.User>(collaborators));

            return activity;
        }
    };

    @Override
    public List<Activity> getActivities(long maxID, long minCreated, long count) {
        String limitedSQL = SqlUtils.applyLimitClause(CONTENT, count, ConnectionManager.getDatabaseType());
        List<Activity> activities = getSimpleJdbcTemplate().query(limitedSQL, mapper, maxID, minCreated);
        if (activities.isEmpty()) {
            return Collections.emptyList();
        }
        return activities;
    }

    @Override
    public long getCount(long minCreated) {
        return executeCountQuery(COUNT, minCreated, getDataSource());
    }

    @Override
    public long getMaxID(long minCreated) {
        return executeCountQuery(MAX, minCreated, getDataSource());
    }

    @Override
    public long getMinID(long minCreated) {
        return executeCountQuery(MIN, minCreated, getDataSource());
    }

}
