package com.jivesoftware.sbs.plugins.foo.impl;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.google.common.primitives.Longs;
import com.jivesoftware.base.UnauthorizedException;
import com.jivesoftware.base.User;
import com.jivesoftware.community.Image;
import com.jivesoftware.community.ImageException;
import com.jivesoftware.community.JiveConstants;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveContentObject.Status;
import com.jivesoftware.community.JiveContext;
import com.jivesoftware.community.JiveIterator;
import com.jivesoftware.community.NotFoundException;
import com.jivesoftware.community.comments.CommentContentResource.StreamRenderType;
import com.jivesoftware.community.comments.CommentDelegator;
import com.jivesoftware.community.comments.CommentManager;
import com.jivesoftware.community.comments.impl.CommentDelegatorImpl;
import com.jivesoftware.community.event.ImageEvent;
import com.jivesoftware.community.impl.DatabaseObjectIterator;
import com.jivesoftware.community.impl.DbImage;
import com.jivesoftware.community.impl.DbImageManager;
import com.jivesoftware.community.impl.ListJiveIterator;
import com.jivesoftware.community.lifecycle.JiveApplication;
import com.jivesoftware.community.objecttype.JiveObjectType;
import com.jivesoftware.community.rating.RatingDelegator;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooImageHelper;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;
import com.jivesoftware.sbs.plugins.foo.impl.FooConverter.BodyProvider;
import com.jivesoftware.util.SimpleDataSource;

public class FooImpl implements Foo {
    private static final Logger log = Logger.getLogger(FooImpl.class);

    private JiveObjectType jiveObjectType;

    private long id;
    private JiveContainer container;
    private User user;
    private Status status;
    private Date creationDate;
    private Date modificationDate;
    private String title;
    private String description;
    private List<Long> images;

    private ViewCountProvider viewCountProvider;
    private BodyProvider body;

    public FooImpl() {
    }

    public FooImpl(long id, User user, JiveContainer container, JiveObjectType objectType, ViewCountProvider viewCountProvider, BodyProvider body, List<Long> images) {
        this.id = id;
        this.user = user;
        this.container = container;
        this.jiveObjectType = objectType;
        this.viewCountProvider = viewCountProvider;
        this.body = body;
        this.images = images;
    }

    @Override
    public JiveObjectType getJiveObjectType() {
        return jiveObjectType;
    }

    @Override
    public int getObjectType() {
        return jiveObjectType == null ? FooObjectType.FOO_TYPE_ID : jiveObjectType.getID();
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public JiveContainer getJiveContainer() {
        return container;
    }

    @Override
    public long getContainerID() {
        return container.getID();
    }

    @Override
    public int getContainerType() {
        return container.getObjectType();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
       this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
       this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Date getModificationDate() {
        return modificationDate;
    }

    @Override
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String getPlainBody() {
        return description;
    }

    @Override
    public String getPlainSubject() {
        return title;
    }

    @Override
    public String getSubject() {
        return title;
    }

    @Override
    public String getUnfilteredSubject() {
        return title;
    }

    @Override
    public Document getBody() {
        return body.get();
    }

    @Override
    public JiveIterator<User> getAuthors() {
        return new ListJiveIterator<User>(Collections.singletonList(getUser()));
    }

    @Override
    public CommentDelegator getCommentDelegator() {
        if (this.getID() == -1) {
            throw new IllegalStateException("Cannot retrieve comment manager prior to foo being saved.");
        }
        return new CommentDelegatorImpl(this, getContext());
    }

    @Override
    public int getCommentStatus() {
        return CommentManager.COMMENTS_OPEN;
    }

    @Override
    public RatingDelegator getRatingDelegator() {
        if (this.getID() == -1) {
            throw new IllegalStateException("Cannot retrieve rating manager prior to foo being saved.");
        }
        return new RatingDelegator(this);
    }

    protected JiveContext getContext() {
        return JiveApplication.getContext();
    }

    @Override
    public int getViewCount() {
        return viewCountProvider.getViewCount(this);
    }

    @Override
    public void addImage(Image image) throws IllegalStateException, ImageException, UnauthorizedException {
        if (image.getJiveContentObject() != null) {
            throw new IllegalStateException("Unable to add image " + image.getID() + " to review " + getID() + ": image is already associated with object with ID "
                + image.getJiveContentObject().getID() + " and type " + image.getJiveContentObject().getObjectType());
        }

        if (getImageCount() > getImageManager().getMaxImagesPerObject()) {
            throw new ImageException(ImageException.TOO_MANY_IMAGES);
        }

        synchronized (images) {
            images.add(image.getID());
        }

        fireImageEvent(image, ImageEvent.Type.ADDED);
    }

    @Override
    public Image createImage(String name, String contentType, InputStream data) throws IllegalStateException, ImageException, UnauthorizedException {
        if (getImageCount() > getImageManager().getMaxImagesPerObject()) {
            throw new ImageException(ImageException.TOO_MANY_IMAGES);
        }

        SimpleDataSource dataSource = new SimpleDataSource();
        dataSource.setContentType(contentType);
        dataSource.setName(name);
        dataSource.setInputStream(data);

        Image image = getImageManager().createImage(this, dataSource);

        synchronized (images) {
            images.add(image.getID());
        }

        fireImageEvent(image, ImageEvent.Type.ADDED);

        return image;
    }

    @Override
    public void deleteImage(Image image) throws ImageException, UnauthorizedException {
        if (!images.contains(image.getID())) {
            throw new IllegalArgumentException("Image " + image.getID() + " does not belong to review " + id);
        }

        DbImage dbImage;
        if (image instanceof DbImage) {
            dbImage = (DbImage) image;
        } else {
            try {
                dbImage = (DbImage) getImage(image.getID());
            } catch (Exception anfe) {
                throw new ImageException(ImageException.GENERAL_ERROR, anfe);
            }
        }

        fireImageEvent(image, ImageEvent.Type.DELETED);

        try {
            FooImageHelper.deleteImage(dbImage);
            synchronized (images) {
                images.remove(images.indexOf(dbImage.getID()));
            }
        } catch (Exception e) {
            throw new ImageException(ImageException.GENERAL_ERROR, e);
        }
    }

    @Override
    public Image getImage(long imageID) throws IllegalArgumentException, ImageException {
        try {
            DbImage image = getImageManager().getImage(imageID);

            if (!images.contains(imageID)) {
                throw new IllegalArgumentException("Image " + imageID + " does not belong to review " + id);
            }

            if (image != null) {
                return image;
            } else {
                throw new ImageException("Unable to load image " + imageID);
            }
        } catch (NotFoundException e) {
            log.error(e.getMessage(), e);
            throw new ImageException(ImageException.GENERAL_ERROR);
        }
    }

    @Override
    public int getImageCount() {
        synchronized (images) {
            return images.size();
        }
    }

    @Override
    public JiveIterator<Image> getImages() {
        return new DatabaseObjectIterator<Image>(JiveConstants.IMAGE, images);
    }

    @Override
    public StreamRenderType getStreamRenderType() {
        return StreamRenderType.PLAINTEXT;
    }

    protected void fireImageEvent(Image image, ImageEvent.Type type) {
        Map<String, Object> paramMap = Collections.emptyMap();
        JiveApplication.getContext().getEventDispatcher().fire(new ImageEvent(type, this, image, paramMap));
    }

    protected DbImageManager getImageManager() {
        return (DbImageManager) getContext().getImageManager();
    }


    @Override
    public Collection<Long> getAuthorIDs() {
        return Longs.asList(getUser().getID());
    }

    @Override
    public long getUserID() {
        return getUser().getID();
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof FooImpl)) {
            return false;
        }

        FooImpl that = (FooImpl)object;

        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.jiveObjectType, that.jiveObjectType);
        builder.append(this.id, that.id);
        builder.append(this.user, that.user);
        builder.append(this.container, that.container);
        builder.append(this.title, that.title);
        builder.append(this.description, that.description);
        builder.append(this.status, that.status);
        builder.append(this.creationDate, that.creationDate);
        builder.append(this.modificationDate, that.modificationDate);

        return builder.isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(jiveObjectType);
        builder.append(id);
        builder.append(user);
        builder.append(container);
        builder.append(title);
        builder.append(description);
        builder.append(status);
        builder.append(creationDate);
        builder.append(modificationDate);

        return builder.hashCode();
    }
}
