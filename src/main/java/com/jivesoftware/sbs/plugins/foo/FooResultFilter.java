package com.jivesoftware.sbs.plugins.foo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.google.common.collect.Lists;
import com.jivesoftware.base.User;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveContentObject.Status;
import com.jivesoftware.community.ResultFilter;

public class FooResultFilter extends ResultFilter {
    private JiveContainer container;
    private User user;
    private List<Status> status = new ArrayList<Status>();

    public static FooResultFilter createDefaultFilter() {
        FooResultFilter resultFilter = new FooResultFilter();
        resultFilter.setSortOrder(DESCENDING);
        resultFilter.getStatus().add(Status.PUBLISHED);
        resultFilter.getStatus().add(Status.ABUSE_VISIBLE);
        return resultFilter;
    }

    public JiveContainer getContainer() {
        return container;
    }

    public void setContainer(JiveContainer container) {
        this.container = container;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStatus(Status... status) {
        this.status = status == null ? null : Lists.newArrayList(status);
    }

    public List<Status> getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof FooResultFilter)) {
            return false;
        }

        FooResultFilter that = (FooResultFilter) o;

        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.container, that.container);
        builder.append(this.user, that.user);
        return builder.isEquals();
    }
}
