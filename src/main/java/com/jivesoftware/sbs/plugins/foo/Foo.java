package com.jivesoftware.sbs.plugins.foo;

import java.util.Date;

import com.jivesoftware.community.ImageContentResource;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveContentObject;
import com.jivesoftware.community.comments.CommentContentResource;
import com.jivesoftware.community.rating.RatingDelegator;

public interface Foo extends JiveContentObject, CommentContentResource, ImageContentResource {

    public void setStatus(Status status);

    public void setCreationDate(Date creationDate);

    public void setModificationDate(Date modificationDate);

    public String getTitle();

    public void setTitle(String title);

    public String getDescription();

    public void setDescription(String description);

    public JiveContainer getJiveContainer();

    public int getViewCount();

    public RatingDelegator getRatingDelegator();
}
