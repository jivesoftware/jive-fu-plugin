package com.jivesoftware.sbs.plugins.foo.provider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.jivesoftware.base.event.ContentEvent;
import com.jivesoftware.base.event.v2.EventListener;
import com.jivesoftware.community.browse.BrowseContentBean;
import com.jivesoftware.community.browse.provider.BaseBrowseDataProvider;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooEvent;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;

/**
 * Enables Foos for the Browse UI
 *
 */
public class FooBrowseDataProvider extends BaseBrowseDataProvider implements EventListener<FooEvent>{
    private static final String MIN_ID = "SELECT MIN(fooID) FROM jiveFoo";
    private static final String MAX_ID = "SELECT MAX(fooID) FROM jiveFoo";
    private static final String CONTENT = ""
        + "SELECT fooID AS objectID, " + FooObjectType.FOO_TYPE_ID + " AS objectType, containerID, containerType, userID AS authorID, status, title AS subject, creationDate, modificationDate, modificationDate AS lastActivityDate "
        + "FROM jiveFoo "
        + "WHERE fooID >= :startID AND fooID <= :endID";
    private static final String CONTENT_MOD = ""
        + "SELECT fooID AS objectID, " + FooObjectType.FOO_TYPE_ID + " AS objectType, containerID, containerType, userID AS authorID, status, title AS subject, creationDate, modificationDate, modificationDate AS lastActivityDate "
        + "FROM jiveFoo "
        + "WHERE modificationDate >= :minModificationDate AND modificationDate <= :maxModificationDate and fooID > :startID";

    @Override
    public int getObjectType() {
        return FooObjectType.FOO_TYPE_ID;
    }

    @Override
    public boolean isProviderOf(DataType dataType) {
        return dataType == DataType.Content;
    }

    @Override
    public long getMaxID(DataType dataType, int objectType) {
        try {
            return template().queryForLong(MAX_ID);
        } catch (EmptyResultDataAccessException e) {
            return -1;
        }
    }

    @Override
    public long getMinID(DataType dataType, int objectType) {
        try {
            return template().queryForLong(MIN_ID);
        } catch (EmptyResultDataAccessException e) {
            return -1;
        }
    }

    @Override
    public List<BrowseContentBean> getContent(long startID, long endID) {
        try {
            return template().query(CONTENT, new BrowseContentBeanRowMapper(), new MapSqlParameterSource(ImmutableMap.of("startID", startID, "endID", endID)));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<BrowseContentBean> getModifiedContent(long minModificationDate, long maxModificationDate, long startID, int limit) {
        return getModifiedContentForSQL(CONTENT_MOD, minModificationDate, maxModificationDate, startID, limit);
    }

    @Override
    public List<Integer> getAssociatedObjectTypes(DataType dataType) {
        return Lists.newArrayList(FooObjectType.FOO_TYPE_ID);
    }

    @Override
    public void handle(FooEvent e) {
        super.handle(e);

        if (e.getContentModificationType() == ContentEvent.ModificationType.Modify) {
            Foo foo = (Foo)loadObject(e.getPayload());
            if (foo != null && (foo.getUser().getID() != e.getActorID())) {
                browseManager.setParticipated(foo.getObjectType(), foo.getID(), e.getActorID());
            }
        }
    }

    protected class BrowseContentBeanRowMapper implements RowMapper<BrowseContentBean> {
        public BrowseContentBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            BrowseContentBean obj = new BrowseContentBean();
            obj.setContainerID(rs.getLong("containerID"));
            obj.setContainerType(rs.getInt("containerType"));
            obj.setObjectType(rs.getInt("objectType"));
            obj.setID(rs.getLong("objectID"));
            obj.setAuthorID(rs.getLong("authorID"));
            obj.setStatusCode(rs.getInt("status"));
            obj.setCreationDateMillis(rs.getLong("creationDate"));
            obj.setModificationDateMillis(rs.getLong("modificationDate"));
            obj.setSubject(rs.getString("subject"));
            obj.setLastActivityDateMillis(rs.getLong("lastActivityDate"));
            return obj;
        }
    }

}
