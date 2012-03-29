package com.jivesoftware.sbs.plugins.foo.provider;

import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.community.JiveConstants;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveContentObject;
import com.jivesoftware.community.ResultFilter;
import com.jivesoftware.community.TagSet;
import com.jivesoftware.community.impl.QueryContainer;
import com.jivesoftware.community.internal.ExtendedCommunityManager;
import com.jivesoftware.community.lifecycle.spring.BeanProvider;
import com.jivesoftware.community.objecttype.AbstractFilteredContentProvider;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;

public class FooFilteredContentProvider extends AbstractFilteredContentProvider {

    private BeanProvider<FooObjectType> fooObjectTypeProvider;
    private ExtendedCommunityManager communityManager;

    @Override
    protected String getIdentifierColumn() {
        return "fooID";
    }

    @Override
    protected int getObjectTypeId() {
        return fooObjectTypeProvider.get().getID();
    }

    @Override
    protected String getTableName() {
        return "jiveFoo";
    }

    @Override
    protected boolean shortCircuitWhenNotRoot() {
        return false;
    }

    @Override
    public QueryContainer getFilteredContentQuery(ResultFilter filter, JiveContainer container, boolean isRoot) {
        boolean filterUser = filter.getUserID() != ResultFilter.NULL_INT;
        boolean filterCreationDate = filter.getCreationDateRangeMin() != null || filter.getCreationDateRangeMax() != null;
        boolean filterModifiedDate = filter.getModificationDateRangeMin() != null || filter.getModificationDateRangeMax() != null;

        QueryContainer query = new QueryContainer();

        query.appendText("SELECT DISTINCT jiveFoo.fooID as objectID, ");
        query.appendText(String.valueOf(getObjectTypeId()));
        query.appendText(" as objectType, jiveFoo.modificationDate as modDate,");
        query.appendText(" jiveFoo.creationDate as createDate");
        query.appendText(" FROM jiveFoo");

        if (!isRoot && filter.isRecursive() && container.getObjectType() == JiveConstants.COMMUNITY) {
            query.appendText(", jiveCommunity");
        }

        if(filter.getTagSets().size() > 0) {
            query.appendText(", jiveObjectTagSet ots");
        }
        query.appendText(" WHERE 1=1");
        if (!isRoot) {
            if (filter.isRecursive() && container.getObjectType() == JiveConstants.COMMUNITY) {
                query.appendText(" AND jiveFoo.containerType = ?");
                query.addArgumentValue(JiveConstants.COMMUNITY);
                query.appendText(" AND jiveFoo.containerID = jiveCommunity.communityID");
                int[] lftRgtValues = communityManager.getLftRgtValues(container.getID());
                query.appendText(" AND jiveCommunity.lft >= ?");
                query.addArgumentValue(lftRgtValues[0]);
                query.appendText(" AND jiveCommunity.rgt <= ?");
                query.addArgumentValue(lftRgtValues[1]);
            } else {
                query.appendText(" AND jiveFoo.containerType = ?");
                query.addArgumentValue(container.getObjectType());
                query.appendText(" AND jiveFoo.containerID = ?");
                query.addArgumentValue(container.getID());
            }
        }

        if (filterUser) {
            query.appendText(" AND jiveFoo.userID = ?");
            query.addArgumentValue(filter.getUserID());
        }

        if (filterCreationDate) {
            if (filter.getCreationDateRangeMin() != null) {
                query.appendText(" AND jiveFoo.creationDate >= ?");
                query.addArgumentValue(filter.getCreationDateRangeMin().getTime());
            }

            if (filter.getCreationDateRangeMax() != null) {
                query.appendText(" AND jiveFoo.creationDate <= ?");
                query.addArgumentValue(filter.getCreationDateRangeMax().getTime());
            }
        }

        if (filterModifiedDate) {
            if (filter.getModificationDateRangeMin() != null) {
                query.appendText(" AND jiveFoo.modificationDate >= ?");
                query.addArgumentValue(filter.getModificationDateRangeMin().getTime());
            }

            if (filter.getModificationDateRangeMax() != null) {
                query.appendText(" AND jiveFoo.modificationDate <= ?");
                query.addArgumentValue(filter.getModificationDateRangeMax().getTime());
            }
        }

        query.appendText(" AND jiveFoo.status in (?,?)");
        query.addArgumentValue(JiveContentObject.Status.PUBLISHED.intValue());
        query.addArgumentValue(JiveContentObject.Status.ABUSE_VISIBLE.intValue());

        if(filter.getTagSets().size() > 0) {
            query.appendText(" AND eventID = ots.objectID AND ots.objectType = " + FooObjectType.FOO_TYPE_ID + " AND ");
            if(filter.getTagSets().size() == 1) {
                query.appendText("ots.tagSetID = ?");
            } else {
                query.appendText("ots.tagSetID in (");
                String sep = "";
                for(int i = 0; i < filter.getTagSets().size(); i++) {
                    query.appendText(sep);
                    query.appendText("?");
                    sep = ",";
                }
                query.appendText(")");
            }

            for(TagSet tagSet : filter.getTagSets()) {
                query.addArgumentValue(tagSet.getID());
            }
        }
        return query;
    }

    @Required
    public void setFooObjectTypeProvider(BeanProvider<FooObjectType> fooObjectTypeProvider) {
        this.fooObjectTypeProvider = fooObjectTypeProvider;
    }

    @Required
    public void setCommunityManager(ExtendedCommunityManager communityManager) {
        this.communityManager = communityManager;
    }

}
