package com.jivesoftware.sbs.plugins.foo.impl;

import java.util.List;

import org.w3c.dom.Document;

import com.jivesoftware.base.User;
import com.jivesoftware.base.UserManager;
import com.jivesoftware.base.UserNotFoundException;
import com.jivesoftware.community.EntityDescriptor;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveContainerManager;
import com.jivesoftware.community.JiveContentObject.Status;
import com.jivesoftware.community.NotFoundException;
import com.jivesoftware.community.impl.RenderCacheManagerImpl;
import com.jivesoftware.community.lifecycle.spring.BeanProvider;
import com.jivesoftware.community.objecttype.JiveObjectType;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooBean;
import com.jivesoftware.sbs.plugins.foo.FooImageHelper;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;

public class FooConverter {
    private static final BodyProvider NULL_BODY_PROVIDER = new BodyProvider(null, null, null) {
        public Document get() {
            return null;
        }
    };

    private UserManager userManager;
    private JiveContainerManager containerManager;
    private RenderCacheManagerImpl renderCacheManager;
    private BeanProvider<FooObjectType> objectTypeProvider;
    private ViewCountProvider viewCountProvider;
    private FooImageHelper imageHelper;

    public Foo convert(FooBean bean) {
        if (bean == null) {
            return null;
        }

        JiveObjectType fooObjectType = objectTypeProvider.get();

        User user;
        try {
            user = userManager.getUser(bean.getUserID());
        } catch (UserNotFoundException e) {
            return null;
        }

        JiveContainer container;
        try {
            container = containerManager.getJiveContainer(bean.getContainerType(), bean.getContainerID());
        } catch (NotFoundException e) {
            return null;
        }

        BodyProvider bodyProvider = this.getBodyProvider(bean, fooObjectType);
        List<Long> images = imageHelper.getImages(bean.getID(), fooObjectType.getID());
        return loadObject(bean, user, container, fooObjectType, viewCountProvider, bodyProvider, images);
    }

    protected FooImpl loadObject(FooBean bean, User user, JiveContainer container, JiveObjectType objectType, ViewCountProvider viewCountProvider, BodyProvider bodyProvider, List<Long> images) {
        FooImpl foo = new FooImpl(bean.getID(), user, container, objectType, viewCountProvider, bodyProvider, images);

        foo.setTitle(bean.getTitle());
        foo.setDescription(bean.getDescription());
        foo.setCreationDate(bean.getCreationDate());
        foo.setModificationDate(bean.getModificationDate());
        foo.setStatus(Status.valueOf(bean.getStatusID()));
        return foo;
    }

    public void setImageHelper(FooImageHelper imageHelper) {
        this.imageHelper = imageHelper;
    }

    protected BodyProvider getBodyProvider(FooBean bean, JiveObjectType fooObjectType) {
        BodyProvider bodyProvider;
        if (bean.getDescription() != null) {
            bodyProvider = new BodyProvider(renderCacheManager, new EntityDescriptor(fooObjectType.getID(), bean.getID()), bean.getDescription());
        } else {
            bodyProvider = NULL_BODY_PROVIDER;
        }

        return bodyProvider;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setContainerManager(JiveContainerManager containerManager) {
        this.containerManager = containerManager;
    }

    public void setRenderCacheManager(RenderCacheManagerImpl renderCacheManager) {
        this.renderCacheManager = renderCacheManager;
    }

    public void setObjectTypeProvider(BeanProvider<FooObjectType> objectTypeProvider) {
        this.objectTypeProvider = objectTypeProvider;
    }

    public void setViewCountProvider(ViewCountProvider viewCountProvider) {
        this.viewCountProvider = viewCountProvider;
    }

    public static class BodyProvider {
        protected final RenderCacheManagerImpl renderCacheManagerImpl;
        protected final EntityDescriptor bookmarkDescriptor;
        protected final String sourceXml;

        public BodyProvider(RenderCacheManagerImpl renderCacheManagerImpl, EntityDescriptor bookmarkDescriptor, String sourceXml) {
            this.renderCacheManagerImpl = renderCacheManagerImpl;
            this.sourceXml = sourceXml;
            this.bookmarkDescriptor = bookmarkDescriptor;
        }

        public org.w3c.dom.Document get() {
            return renderCacheManagerImpl.retrieveXmlDocument(bookmarkDescriptor, sourceXml);
        }

        public String getSourceXml() {
            return sourceXml;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof BodyProvider)) {
                return false;
            }

            BodyProvider that = (BodyProvider) o;

            return !(sourceXml != null ? !sourceXml.equals(that.sourceXml) : that.sourceXml != null);
        }

        public int hashCode() {
            return (sourceXml != null ? sourceXml.hashCode() : 0);
        }

        public String toString() {
            return String.format("BodyProvider{bookmarkDescriptor=%s, sourceXml='%s'}", bookmarkDescriptor, sourceXml);
        }
    }
}
