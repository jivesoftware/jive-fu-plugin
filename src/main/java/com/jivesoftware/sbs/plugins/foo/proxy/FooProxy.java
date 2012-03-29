package com.jivesoftware.sbs.plugins.foo.proxy;

import java.io.InputStream;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;

import com.jivesoftware.base.AuthToken;
import com.jivesoftware.base.UnauthorizedException;
import com.jivesoftware.base.User;
import com.jivesoftware.base.proxy.JiveProxy;
import com.jivesoftware.base.proxy.ProxyUtils;
import com.jivesoftware.community.Image;
import com.jivesoftware.community.ImageException;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveIterator;
import com.jivesoftware.community.aaa.authz.EntitlementTypeProvider.EntitlementType;
import com.jivesoftware.community.comments.CommentDelegator;
import com.jivesoftware.community.objecttype.EntitlementCheckProvider;
import com.jivesoftware.community.objecttype.JiveObjectType;
import com.jivesoftware.community.proxy.ImageProxy;
import com.jivesoftware.community.proxy.IteratorProxy;
import com.jivesoftware.community.rating.RatingDelegator;
import com.jivesoftware.sbs.plugins.foo.Foo;

public class FooProxy implements Foo, JiveProxy<Foo> {
    private Foo foo;
    private AuthToken authToken;
    private EntitlementCheckProvider<Foo> entitlementCheckProvider;

    @Override
    public AuthToken getProxyAuthToken() {
        return authToken;
    }

    @Override
    public Foo getUnproxiedObject() {
        return foo;
    }

    @Override
    public void init(Foo target, AuthToken authToken) {
        this.foo = target;
        this.authToken = authToken;
    }

    protected boolean isAllowedToEdit() {
        return entitlementCheckProvider.isUserEntitled(foo, EntitlementType.EDIT);
    }

    @Override
    public long getContainerID() {
        return foo.getContainerID();
    }

    @Override
    public int getContainerType() {
        return foo.getContainerType();
    }

    @Override
    public long getID() {
        return foo.getID();
    }

    @Override
    public int getObjectType() {
        return foo.getObjectType();
    }

    @Override
    public JiveObjectType getJiveObjectType() {
        return foo.getJiveObjectType();
    }

    @Override
    public String getTitle() {
        return foo.getTitle();
    }

    @Override
    public void setTitle(String title) {
        if (isAllowedToEdit()) {
            foo.setTitle(title);
        } else {
            throw new UnauthorizedException();
        }

    }

    @Override
    public String getDescription() {
        return foo.getDescription();
    }

    @Override
    public void setDescription(String description) {
        if (isAllowedToEdit()) {
            foo.setDescription(description);
        } else {
            throw new UnauthorizedException();
        }

    }


    @Override
    public int hashCode() {
        return foo.hashCode();
    }

    @Override
    public Document getBody() {
        return foo.getBody();
    }

    @Override
    public Date getCreationDate() {
        return foo.getCreationDate();
    }

    @Override
    public Date getModificationDate() {
        return foo.getModificationDate();
    }

    @Override
    public String getPlainBody() {
        return foo.getPlainBody();
    }

    @Override
    public String getPlainSubject() {
        return foo.getPlainSubject();
    }

    @Override
    public Status getStatus() {
        return foo.getStatus();
    }

    @Override
    public String getSubject() {
        return foo.getSubject();
    }

    @Override
    public String getUnfilteredSubject() {
        return foo.getUnfilteredSubject();
    }

    @Override
    public User getUser() {
        return foo.getUser();
    }

    @Override
    public JiveIterator<User> getAuthors() {
        return foo.getAuthors();
    }

    public void setCreationDate(Date creationDate) {
        if (isAllowedToEdit()) {
            foo.setCreationDate(creationDate);
        } else {
            throw new UnauthorizedException();
        }
    }

    public void setModificationDate(Date modificationDate) {
        if (isAllowedToEdit()) {
            foo.setModificationDate(modificationDate);
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public CommentDelegator getCommentDelegator() {
        return foo.getCommentDelegator();
    }

    @Override
    public int getCommentStatus() {
        return foo.getCommentStatus();
    }

    @Override
    public StreamRenderType getStreamRenderType() {
        return foo.getStreamRenderType();
    }

	@Override
	public RatingDelegator getRatingDelegator() {
	   return foo.getRatingDelegator();
	}

    @Override
    public long getUserID() {
        return foo.getUserID();
    }

    @Override
    public Collection<Long> getAuthorIDs() {
        return foo.getAuthorIDs();
    }

    public void setStatus(Status status) {
        if (isAllowedToEdit()) {
            foo.setStatus(status);
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public JiveContainer getJiveContainer() {
        return foo.getJiveContainer();
    }

    @Required
    public void setEntitlementCheckProvider(EntitlementCheckProvider<Foo> entitlementCheckProvider) {
        this.entitlementCheckProvider = entitlementCheckProvider;
    }

    @Override
    public int getViewCount() {
        return foo.getViewCount();
    }

    @Override
    public void addImage(Image image) throws IllegalStateException, ImageException, UnauthorizedException {
        if (isAllowedToEdit()) {
            foo.addImage(image);
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public Image createImage(String name, String contentType, InputStream data) throws IllegalStateException, ImageException, UnauthorizedException {
        if (isAllowedToEdit()) {
            return ProxyUtils.proxyObject(ImageProxy.class, foo.createImage(name, contentType, data), authToken);
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public void deleteImage(Image image) throws ImageException, UnauthorizedException {
        if (isAllowedToEdit()) {
            foo.deleteImage(image);
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public Image getImage(long imageID) throws IllegalArgumentException, ImageException {
        return ProxyUtils.proxyObject(ImageProxy.class, foo.getImage(imageID), authToken);
    }

    @Override
    public int getImageCount() {
        return foo.getImageCount();
    }

    @Override
    public JiveIterator<Image> getImages() {
        return new IteratorProxy<Image>(foo.getImages(), authToken);
    }
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (object instanceof FooProxy) {
            return foo.equals(((FooProxy) object).foo);
        } else {
            return foo.equals(object);
        }
    }

    @Override
    public String toString() {
        return foo.toString();
    }

}
