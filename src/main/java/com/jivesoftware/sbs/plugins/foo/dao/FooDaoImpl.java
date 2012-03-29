package com.jivesoftware.sbs.plugins.foo.dao;

import java.text.MessageFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.jivesoftware.base.User;
import com.jivesoftware.base.database.EntityDescriptorRowMapper;
import com.jivesoftware.base.database.LongRowMapper;
import com.jivesoftware.base.database.dao.DAOException;
import com.jivesoftware.base.database.dao.JiveJdbcDaoSupport;
import com.jivesoftware.base.database.dao.SequenceProvider;
import com.jivesoftware.community.ContentTag;
import com.jivesoftware.community.EntityDescriptor;
import com.jivesoftware.community.JiveConstants;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveContentObject;
import com.jivesoftware.community.JiveContentObject.Status;
import com.jivesoftware.community.ResultFilter;
import com.jivesoftware.community.TagSet;
import com.jivesoftware.community.impl.CachedPreparedStatement;
import com.jivesoftware.community.lifecycle.JiveApplication;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooBean;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;
import com.jivesoftware.sbs.plugins.foo.FooResultFilter;

public class FooDaoImpl extends JiveJdbcDaoSupport implements FooDao {
    private static final Logger log = Logger.getLogger(FooDaoImpl.class);

    private static final String SELECT_SQL = ""
        + " SELECT fooID, containerID, containerType, userID, status, creationDate, modificationDate, title, description "
        + "   FROM jiveFoo " + "  WHERE fooID = ? ";

    private static final String SELECT_ALL_FOO_IDS_FOR_CONTAINER_SQL = ""
        + " SELECT fooID FROM jiveFoo WHERE containerID = ? AND containerType = ?";

    private static final String SELECT_ALL_FOO_IDS_FOR_USER_SQL = ""
        + " SELECT fooID FROM jiveFoo WHERE userId = ?";

    private static final String INSERT_SQL = ""
        + " INSERT INTO jiveFoo ( "
        + "        fooID, containerID, containerType, userID, status, creationDate, modificationDate, title, description "
        + " ) VALUES ( "
        + "        :ID, :containerID, :containerType, :userID, :statusID, :creationDate, :modificationDate, :title, :description "
        + " ); ";



    private static final String UPDATE_SQL = ""
        + " UPDATE jiveFoo "
        + "    SET containerID = :containerID, containerType = :containerType, userID = :userID, status = :statusID, creationDate = :creationDate, modificationDate = :modificationDate, title = :title, description = :description "
        + " WHERE fooID = :ID ";



    private static final String DELETE_SQL = "" + " DELETE FROM jiveFoo WHERE fooID = ? ";

    private static final String MOVE_SQL = ""
        + " UPDATE jiveFoo SET containerID = ?, containerType = ? WHERE fooID =? AND containerID = ? and containerType = ?";

    private static final String FREQUENT_FOO_CONTAINERS = "SELECT containerType, containerID from jiveFoo"
        + " WHERE userID = ? GROUP BY containerType, containerID ORDER BY MAX(creationDate) DESC ";

    private static final String SELECT = "SELECT";
    private static final String COUNT = " count(*)";
    private static final String FOO_FIELDS = " DISTINCT fooId, modificationDate";
    private static final String FROM = " FROM";
    private static final String JIVE_FOO = " jiveFoo ";
    private static final String WHERE = " WHERE 1=1";
    private static final String CONTAINER = "containerId = ? and containerType = ?";
    private static final String AND = " AND ";
    private static final String OR = " OR ";
    private static final String USER = "userId = ?";
    private static final String STATUS_IS = "status = ?";

    private static final String JIVE_OBJECT_TAG = " jiveObjectTag ot";
    private static final String JIVE_OBJECT_TAG_SET = " jiveObjectTagSet ots";

    private static final String TAGS_SUBQUERY = ""
        + "fooID IN (SELECT objectID FROM jiveObjectTag WHERE objectType = " + FooObjectType.FOO_TYPE_ID
        + " AND tagID IN ({0})"
        + " GROUP BY objectID HAVING COUNT(*) >= {1})";
    private static final String TAGS_JOIN = "ot.objectID = fooID and ot.objectType = " + FooObjectType.FOO_TYPE_ID
        + " AND ot.tagID in ({0})";
    private static final String TAG_SETS_JOIN = "fooID = ots.objectID AND ots.objectType = " + FooObjectType.FOO_TYPE_ID;
    private static final String TAG_SETS_SINGLE = "ots.tagSetID = ?";
    private static final String TAG_SETS_MULTIPLE = "ots.tagSetID in ({0})";
    private static final String ORDER_BY = " order by ";
    private static final String MODIFICATION_DATE = "modificationDate";
    private static final String DESCENDING = " desc";

    private RowMapper<FooBean> rowMapper;
    private SequenceProvider sequenceProvider;

    @Override
    public CachedPreparedStatement getFooListSQL(FooResultFilter resultFilter) {
        return this.getFooSQL(resultFilter, false);
    }

    @Override
    public CachedPreparedStatement getFooListCountSQL(FooResultFilter resultFilter) {
        return this.getFooSQL(resultFilter, true);
    }

    protected CachedPreparedStatement getFooSQL(FooResultFilter resultFilter, boolean countQuery) {
        JiveContainer container = resultFilter.getContainer();
        boolean isRoot = container == null
                || (container.getObjectType() == JiveConstants.COMMUNITY && container.getID() == JiveApplication.getContext().getCommunityManager().getRootCommunity().getID());

        boolean forContainer = resultFilter.getContainer() != null;
        boolean forUser = resultFilter.getUser() != null;
        List<JiveContentObject.Status> statusMatches = resultFilter.getStatus();


        StringBuilder sql = new StringBuilder(SELECT);
        sql.append(countQuery ? COUNT : FOO_FIELDS);
        sql.append(FROM);

        sql.append(JIVE_FOO);

        if (resultFilter.getTagSets().size() > 0) {
            sql.append(",").append(JIVE_OBJECT_TAG_SET);
        }

        if (resultFilter.getTags().size() > 0 && !resultFilter.isAllTagsRequired()) {
            sql.append(",").append(JIVE_OBJECT_TAG);
        }
        sql.append(WHERE);

        if (forContainer && !isRoot) {
            sql.append(AND);
            sql.append(CONTAINER);
        }

        if (forUser) {
            sql.append(AND);
            sql.append(USER);
        }

        if(statusMatches != null && !statusMatches.isEmpty()) {
            sql.append(AND);

            sql.append("(");

            for(int i = 0; i < statusMatches.size(); i++) {
                if (forContainer) {
                    sql.append("jiveFoo.");
                }

                sql.append(STATUS_IS);

                if(i < statusMatches.size() - 1) {
                    sql.append(OR);
                }
            }

            sql.append(")");
        }

        if (resultFilter.getTagSets().size() > 0) {
            sql.append(AND);
            sql.append(TAG_SETS_JOIN);
            sql.append(AND);

            if(resultFilter.getTagSets().size() == 1) {
                sql.append(TAG_SETS_SINGLE);
            } else {
                StringBuffer tagSetParams = new StringBuffer();
                String sep = "";
                for(TagSet set : resultFilter.getTagSets()) {
                    tagSetParams.append(sep).append("?");
                    sep = ",";
                }
                sql.append(MessageFormat.format(TAG_SETS_MULTIPLE, tagSetParams));
            }
        }

        if (resultFilter.getTags().size() > 0) {
            StringBuffer tags = new StringBuffer();
            String sep = "";
            for(ContentTag tag : resultFilter.getTags()) {
                tags.append(sep).append("?");
                sep = ",";
            }

            sql.append(AND);
            if (resultFilter.isAllTagsRequired()) {
                sql.append(MessageFormat.format(TAGS_SUBQUERY, tags.toString(), resultFilter.getTags().size()));
            } else {
                sql.append(MessageFormat.format(TAGS_JOIN, tags.toString()));
            }
        }

        // order by
        if (!countQuery) {
            sql.append(ORDER_BY).append(MODIFICATION_DATE).append(resultFilter.getSortOrder() == ResultFilter.DESCENDING ? DESCENDING : "");
        }

        CachedPreparedStatement ps = new CachedPreparedStatement(sql.toString());

        if (forContainer && !isRoot) {
            ps.addLong(resultFilter.getContainer().getID());
            ps.addInt(resultFilter.getContainer().getObjectType());
        }

        if (forUser) {
            ps.addLong(resultFilter.getUser().getID());
        }

        if(statusMatches != null && !statusMatches.isEmpty()) {
            for(Status status : statusMatches) {
                ps.addInt(status.intValue());
            }
        }

        if(resultFilter.getTagSets() != null) {
            for(TagSet tagSet : resultFilter.getTagSets()) {
                ps.addLong(tagSet.getID());
            }
        }

        if(resultFilter.getTags() != null) {
            for(ContentTag tag : resultFilter.getTags()) {
                ps.addLong(tag.getID());
            }
        }
        return ps;
    }

    @Override
    public List<Long> getAllFooIDs(JiveContainer container) {
        return getSimpleJdbcTemplate().query(SELECT_ALL_FOO_IDS_FOR_CONTAINER_SQL, LongRowMapper.getLongRowMapper(), container.getID(), container.getObjectType());
    }

    @Override
    public List<Long> getAllFooIDs(User user) {
        return getSimpleJdbcTemplate().query(SELECT_ALL_FOO_IDS_FOR_USER_SQL, LongRowMapper.getLongRowMapper(), user.getID());
    }

    @Override
    public void deleteFoos(List<Long> fooIDs) {
        for(long fooID : fooIDs) {
            deleteFoo(fooID);
        }
    }

    @Override
    public void deleteFoo(long id) {
        getSimpleJdbcTemplate().update(DELETE_SQL, id);
    }

    @Override
    public FooBean getFoo(long id) {
        try {
           return getSimpleJdbcTemplate().queryForObject(SELECT_SQL, rowMapper, id);
        } catch(EmptyResultDataAccessException e) {
           return null;
        }
    }

    @Override
    public long saveFoo(FooBean bean) {
        long fooId = sequenceProvider.nextId();
        bean.setID(fooId);
        SqlParameterSource namedParameters = new JiveBeanPropertySqlParameterSource(bean);
        getSimpleJdbcTemplate().update(INSERT_SQL, namedParameters);

        return fooId;
    }

    @Override
    public void updateFoo(FooBean object) {
        SqlParameterSource namedParameters = new JiveBeanPropertySqlParameterSource(object);
        getSimpleJdbcTemplate().update(UPDATE_SQL, namedParameters);
    }

    @Override
    public void moveFoo(Foo foo, JiveContainer destination) {
        getSimpleJdbcTemplate().update(MOVE_SQL, destination.getID(), destination.getObjectType(), foo.getID(), foo.getContainerID(), foo.getContainerType());
    }

    @Override
    public List<EntityDescriptor> getFrequentFooContainers(long userID) {
        try {
            return getSimpleJdbcTemplate().query(FREQUENT_FOO_CONTAINERS, new EntityDescriptorRowMapper(), userID);
        }
        catch (Exception ex) {
            final String message = "Failed to retrieve frequent foo containers for user: " + userID;
            log.error(message, ex);
            throw new DAOException(message, ex);
        }
    }

    @Required
    public void setRowMapper(RowMapper<FooBean> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Required
    public void setSequenceProvider(SequenceProvider sequenceProvider) {
        this.sequenceProvider = sequenceProvider;
    }
}
