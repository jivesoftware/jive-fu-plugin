package com.jivesoftware.sbs.plugins.foo.provider;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.base.User;
import com.jivesoftware.community.EntityDescriptor;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveContentObject;
import com.jivesoftware.community.JiveContentObject.Status;
import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.RecentContentEventHandlingStrategy;
import com.jivesoftware.community.RecentContentInfoProvider;
import com.jivesoftware.community.action.util.RenderUtils;
import com.jivesoftware.community.impl.QueryContainer;
import com.jivesoftware.community.lifecycle.JiveApplication;
import com.jivesoftware.community.web.JiveResourceResolver;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.dao.FooDao;
import com.jivesoftware.util.DateUtils;

public class FooRecentContentInfoProvider implements RecentContentInfoProvider {

    private FooDao fooDao;

    @Override
    public boolean eventsHandledExclusivelyByCustomListener() {
        return false;
    }

    @Override
    public User getAuthor(JiveObject object) {
        return ((Foo) object).getUser();
    }

    @Override
    public QueryContainer getContentContainerInfoQuery(List<Long> objectIDs, EntityDescriptor container) {
        QueryContainer sql = new QueryContainer();

        sql.appendText("SELECT fooID, containerType, containerID FROM jiveFoo WHERE fooID in (");

        String sep = "";
        for(long objectID : objectIDs) {
            sql.appendText(sep);
            sql.appendText(String.valueOf(objectID));
            sep = ",";
        }

        sql.appendText(") AND status = ").appendText(String.valueOf(Status.PUBLISHED.intValue()));

        return sql;
    }

    @Override
    public String getDate(JiveObject object) {
        return new DateUtils().displayFriendly(((Foo) object).getModificationDate());
    }

    @Override
    public User getEditingUser(JiveObject object) {
        return null;
    }

    @Override
    public RecentContentEventHandlingStrategy getEventHandlingStrategy() {
        return null;
    }

    @Override
    public String getObjectUrl(JiveObject object, boolean constructAbsoluteURL) {
        return JiveResourceResolver.getJiveObjectURL(object, constructAbsoluteURL);
    }

    @Override
    public JiveContainer getParent(JiveObject object) {
        return ((Foo) object).getJiveContainer();
    }

    @Override
    public List<EntityDescriptor> getRecentContentContainersForUser(User user) {
        return fooDao.getFrequentFooContainers(user.getID());
    }

    @Override
    public String getSubject(JiveObject object) {
        return RenderUtils.renderSubjectToText((JiveContentObject) object, JiveApplication.getContext().getRenderManager());
    }

    @Required
    public void setFooDao(FooDao fooDao) {
        this.fooDao = fooDao;
    }

}
