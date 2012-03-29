package com.jivesoftware.sbs.plugins.foo.impl;


import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jivesoftware.base.User;
import com.jivesoftware.base.aaa.AuthenticationProvider;
import com.jivesoftware.base.event.v2.EventDispatcher;
import com.jivesoftware.cache.Cache;
import com.jivesoftware.community.ContainerAwareEntityDescriptor;
import com.jivesoftware.community.Image;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveContentObject.Status;
import com.jivesoftware.community.JiveInterceptor;
import com.jivesoftware.community.JiveIterator;
import com.jivesoftware.community.NotFoundException;
import com.jivesoftware.community.RenderCacheManager;
import com.jivesoftware.community.ResultFilter;
import com.jivesoftware.community.TagManager;
import com.jivesoftware.community.impl.CachedPreparedStatement;
import com.jivesoftware.community.impl.ObjectFactory;
import com.jivesoftware.community.impl.ProxyBypassHelper;
import com.jivesoftware.community.impl.QueryCacheManager;
import com.jivesoftware.community.internal.ExtendedCommunityManager;
import com.jivesoftware.community.internal.InvocableInterceptorManager;
import com.jivesoftware.community.moderation.ModerationUtil;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooBean;
import com.jivesoftware.sbs.plugins.foo.FooEvent;
import com.jivesoftware.sbs.plugins.foo.FooImageHelper;
import com.jivesoftware.sbs.plugins.foo.FooManager;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;
import com.jivesoftware.sbs.plugins.foo.FooResultFilter;
import com.jivesoftware.sbs.plugins.foo.dao.FooDao;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class FooManagerImpl implements FooManager, ObjectFactory<Foo> {
    protected static final Logger log = Logger.getLogger(FooManagerImpl.class);

    private Cache<Long, FooBean> fooCache;
    private FooDao fooDao;
    private QueryCacheManager queryCacheManager;
    private TagManager tagManager;
    private FooConverter fooConverter;
    private EventDispatcher eventDispatcher;
    private RenderCacheManager renderCacheManager;
    private InvocableInterceptorManager systemInterceptorManager;
    private ExtendedCommunityManager communityManager;
    private AuthenticationProvider authenticationProvider;
    private FooImageHelper imageHelper;

    @Override
    public int getFooCount(FooResultFilter resultFilter) {
        if(resultFilter.getContainer() == null) {
            resultFilter.setContainer(getRootCommunity());
        }

        CachedPreparedStatement cachedPstmt = fooDao.getFooListCountSQL(resultFilter);

        return queryCacheManager.getCount(cachedPstmt, resultFilter.getContainer().getObjectType(), resultFilter.getContainer().getID());
    }

    @Override
    public JiveIterator<Foo> getFoos(FooResultFilter resultFilter) {
        if(resultFilter.getContainer() == null) {
            resultFilter.setContainer(getRootCommunity());
        }


        CachedPreparedStatement cachedPstmt = fooDao.getFooListSQL(resultFilter);

        int startIndex = resultFilter.getStartIndex();
        int endIndex;

        if (resultFilter.getNumResults() == ResultFilter.NULL_INT) {
            endIndex = getFooCount(resultFilter);
        } else {
            endIndex = resultFilter.getNumResults() + startIndex;
        }

        return queryCacheManager.getList(cachedPstmt, startIndex, endIndex, new FooObjectType().getID(), resultFilter.getContainer().getObjectType(), resultFilter.getContainer().getID());
    }

    public void deleteFoo(Foo foo) {
        fireFooDeleting(foo);
        doDeleteFoo(foo);
        fireFooDeleted(foo);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    protected void doDeleteFoo(Foo foo) {
        fooDao.deleteFoo(foo.getID());

        clearCache(foo);
        tagManager.removeAllTags(foo);
        this.updateContainer(foo);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteFoos(JiveContainer container) {
        deleteFoos(fooDao.getAllFooIDs(container));
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteFoos(User user) {
        deleteFoos(fooDao.getAllFooIDs(user));
    }

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    protected void deleteFoos(List<Long> fooIDs) {
        for(long fooID : fooIDs) {
            Foo foo = this.getFoo(fooID);
            deleteFoo(foo);
        }
    }

    @Override
    public Foo getFoo(long id) {
        try {
            return loadObject(id);
        } catch (NotFoundException e) {
            return null;
        }
    }

    public Foo saveFoo(FooBean bean, JiveIterator<Image> images) {
        Foo foo = fooConverter.convert(bean);

        // Run the "pre" interceptors
        InvocableInterceptorManager manager = ((InvocableInterceptorManager) foo.getJiveContainer()
        .getInterceptorManager());

        systemInterceptorManager.invokeInterceptors(foo, JiveInterceptor.Type.TYPE_PRE);
        manager.invokeInterceptors(foo, JiveInterceptor.Type.TYPE_PRE);

        foo = doSaveFoo(bean, images);

        systemInterceptorManager.invokeInterceptors(foo, JiveInterceptor.Type.TYPE_POST);
        manager.invokeInterceptors(foo, JiveInterceptor.Type.TYPE_POST);

        this.fireFooAdded(foo);
        return foo;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    protected Foo doSaveFoo(FooBean bean, JiveIterator<Image> images) {
        try {
            Foo foo = loadObject(fooDao.saveFoo(bean), false);
            imageHelper.saveImages(foo, images);
            clearCache(foo);
            this.updateContainer(foo);

            return foo;
        } catch(NotFoundException e) {
            throw new IllegalStateException("Error loading foo after creation", e);
        }
    }

    @Override
    public Foo updateFoo(Foo foo, JiveIterator<Image> images) {
        Foo updated = doUpdateFoo(foo, images);
        this.fireFooUpdated(updated);
        return updated;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    protected Foo doUpdateFoo(Foo foo, JiveIterator<Image> images) {
        Foo original = getFoo(foo.getID());

        FooBean bean;

        try {
            bean = (FooBean) loadBean(foo.getID(), true).clone();
        } catch(CloneNotSupportedException e) {
            throw new IllegalStateException(String.format("Clone of bean %s failed", foo), e);
        }

        bean.setTitle(foo.getTitle());
        bean.setDescription(foo.getDescription());

        bean.setModificationDate(new Date());
        bean.setStatusID(foo.getStatus().intValue());

        fooDao.updateFoo(bean);

        Foo updated;
        try {
            updated = loadObject(foo.getID(), false);
            if (images != null) {
                imageHelper.saveImages(updated, images);
            }
            this.updateContainer(updated);
        } catch(NotFoundException e) {
            throw new RuntimeException("Error loading bean while updating", e);
        }

        if (isContentUpdate(original, updated)) {
            try {
                InvocableInterceptorManager manager = ((InvocableInterceptorManager) updated.getJiveContainer()
                        .getInterceptorManager());
                manager.invokeInterceptors(updated, JiveInterceptor.Type.TYPE_EDIT);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        clearCache(updated);
        return updated;
    }

    protected boolean isContentUpdate(Foo before, Foo after) {
     	if (ModerationUtil.isContentUpdate(before, after)) {
            return true;
        }

        //Object types with non-standard content (i.e., other than simply subject and
        //body) can extend this method  determine if it has been "updated" or not.
        return false;
    }

    @Override
    public Foo updateFooStatus(Foo foo, Status status) {
        Foo updated = this.doStatusUpdate(foo, status);

        clearCache(updated);
        this.fireFooModerated(updated);
        return updated;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    protected Foo doStatusUpdate(Foo foo, Status status) {
        FooBean bean;

        try {
            bean = (FooBean) loadBean(foo.getID(), true).clone();
        } catch(CloneNotSupportedException e) {
            throw new IllegalStateException(String.format("Clone of bean %s failed", foo), e);
        }

        bean.setStatusID(status.intValue());

        fooDao.updateFoo(bean);

        Foo updated;
        try {
            updated = loadObject(foo.getID(), false);
            this.updateContainer(updated);
        } catch(NotFoundException e) {
            throw new RuntimeException("Error loading bean while updating", e);
        }

        return updated;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Foo moveFoo(Foo foo, JiveContainer destination) {
        systemInterceptorManager.invokeInterceptors(foo, JiveInterceptor.Type.TYPE_PRE);
        renderCacheManager.clearContentCache(foo);

        JiveContainer previous = foo.getJiveContainer();
        Date moved = new Date();
        fooDao.moveFoo(foo, destination);

        FooBean bean = this.loadBean(foo.getID(), false);
        bean.setContainerID(destination.getID());
        bean.setContainerType(destination.getObjectType());

        cacheBean(bean);

        systemInterceptorManager.invokeInterceptors(foo, JiveInterceptor.Type.TYPE_EDIT);

        ProxyBypassHelper.setModificationDateForContainer(destination, moved, authenticationProvider.getJiveUser(), foo.getObjectType());
        ProxyBypassHelper.setModificationDateForContainer(previous, moved, authenticationProvider.getJiveUser(), foo.getObjectType());

        communityManager.clearCache(previous);
        communityManager.clearCache(destination);

        queryCacheManager.removeQueriesForObject(new FooObjectType().getID(), foo.getID());

        this.fireFooMoved(foo);

        return this.getFoo(foo.getID());
    }

    @Override
    public Foo loadObject(String id) throws NotFoundException {
        return loadObject(Long.parseLong(id));
    }

    @Override
    public Foo loadObject(long id) throws NotFoundException {
        return loadObject(id, true);
    }

    public Foo loadObject(long id, boolean useCache) throws NotFoundException {
        return fooConverter.convert(loadBean(id, useCache));
    }

    protected FooBean loadBean(long id, boolean useCache) {
        if(useCache) {
            FooBean bean = fooCache.get(id);
            if(bean != null) {
                return bean;
            }
        }

        return cacheBean(fooDao.getFoo(id));
    }

    protected FooBean cacheBean(FooBean bean) {
        if(bean == null) {
            return null;
        }

        fooCache.put(bean.getID(), bean);

        return bean;
    }

    protected void clearCache(Foo foo) {
        fooCache.remove(foo.getID());
        queryCacheManager.removeQueriesForObject(foo.getObjectType(), foo.getID());
        queryCacheManager.removeContainerQueries(foo.getJiveContainer());
        queryCacheManager.removeContainerQueries(communityManager.getRootCommunity());
        renderCacheManager.clearContentCache(foo);
    }

    protected void updateContainer(Foo foo) {
        ProxyBypassHelper.setModificationDateForContainer(foo.getJiveContainer(), new Date(), foo.getUser(), foo.getObjectType());
    }

    protected boolean areContainersSame(JiveContainer to, JiveContainer from) {
        return to.getObjectType() == from.getObjectType() && to.getID() == from.getID();
    }

    protected JiveContainer getRootCommunity() {
        return communityManager.getRootCommunity();
    }

    protected void fireFooAdded(Foo foo) {
         Future<FooEvent> f = eventDispatcher.fireWithFuture(new FooEvent(FooEvent.Type.CREATED, new ContainerAwareEntityDescriptor(foo.getObjectType(), foo.getID(), foo.getJiveContainer().getID(), foo.getJiveContainer().getObjectType()), foo.getUser().getID(), foo.getStatus()));
         try {
             f.get();
         } catch (ExecutionException e) {
             log.error("Could not fire added event for foo " + foo.getID(), e);
         } catch (InterruptedException e) {
             log.error("Could not fire added event for foo " + foo.getID(), e);
         }
    }

    protected void fireFooUpdated(Foo foo) {
        Future<FooEvent> f = eventDispatcher.fireWithFuture(new FooEvent(FooEvent.Type.MODIFIED, new ContainerAwareEntityDescriptor(foo.getObjectType(), foo.getID(), foo.getJiveContainer().getID(), foo.getJiveContainer().getObjectType()), foo.getUser().getID(), foo.getStatus()));
        try {
            f.get();
        } catch (ExecutionException e) {
            log.error("Could not fire updated event for foo " + foo.getID(), e);
        } catch (InterruptedException e) {
            log.error("Could not fire updated event for foo " + foo.getID(), e);
        }
    }

    protected void fireFooModerated(Foo foo) {
        Future<FooEvent> f = eventDispatcher.fireWithFuture(new FooEvent(FooEvent.Type.MODERATED, new ContainerAwareEntityDescriptor(foo.getObjectType(), foo.getID(), foo.getJiveContainer().getID(), foo.getJiveContainer().getObjectType()), foo.getUser().getID(), foo.getStatus()));
        try {
            f.get();
        } catch (ExecutionException e) {
            log.error("Could not fire moderated event for foo " + foo.getID(), e);
        } catch (InterruptedException e) {
            log.error("Could not fire moderated event for foo " + foo.getID(), e);
        }
    }

    protected void fireFooDeleting(Foo foo) {
        Future<FooEvent> f = eventDispatcher.fireWithFuture(new FooEvent(FooEvent.Type.DELETING, new ContainerAwareEntityDescriptor(foo.getObjectType(), foo.getID(), foo.getJiveContainer().getID(), foo.getJiveContainer().getObjectType()), foo.getUser().getID(), foo.getStatus()));
        try {
            f.get();
        } catch (ExecutionException e) {
            log.error("Could not fire deleting event for foo " + foo.getID(), e);
        } catch (InterruptedException e) {
            log.error("Could not fire deleting event for foo " + foo.getID(), e);
        }
    }

    protected void fireFooDeleted(Foo foo) {
        eventDispatcher.fire(new FooEvent(FooEvent.Type.DELETED, new ContainerAwareEntityDescriptor(foo.getObjectType(), foo.getID(), foo.getJiveContainer().getID(), foo.getJiveContainer().getObjectType()), foo.getUser().getID(), foo.getStatus()));

    }

    protected void fireFooMoved(Foo foo) {
        Future<FooEvent> f = eventDispatcher.fireWithFuture(new FooEvent(FooEvent.Type.MOVED, new ContainerAwareEntityDescriptor(foo.getObjectType(), foo.getID(), foo.getJiveContainer().getID(), foo.getJiveContainer().getObjectType()), foo.getUser().getID(), foo.getStatus()));
        try {
            f.get();
        } catch (ExecutionException e) {
            log.error("Could not fire moved event for foo " + foo.getID(), e);
        } catch (InterruptedException e) {
            log.error("Could not fire moved event for foo " + foo.getID(), e);
        }
    }

    @Required
    public void setFooDao(FooDao fooDao) {
        this.fooDao = fooDao;
    }

    @Required
    public void setQueryCacheManager(QueryCacheManager queryCacheManager) {
        this.queryCacheManager = queryCacheManager;
    }

    @Required
    public void setFooCache(Cache<Long, FooBean> fooCache) {
        this.fooCache = fooCache;
    }

    @Required
    public void setImageHelper(FooImageHelper imageHelper) {
        this.imageHelper = imageHelper;
    }

    @Required
    public void setFooConverter(FooConverter fooConverter) {
        this.fooConverter = fooConverter;
    }

    @Required
    public void setEventDispatcher(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Required
    public void setInterceptorManager(InvocableInterceptorManager interceptorManager) {
        this.systemInterceptorManager = interceptorManager;
    }

    @Required
    public void setTagManager(TagManager tagManager) {
        this.tagManager = tagManager;
    }

    @Required
    public void setRenderCacheManager(RenderCacheManager renderCacheManager) {
        this.renderCacheManager = renderCacheManager;
    }

    @Required
    public void setCommunityManager(ExtendedCommunityManager communityManager) {
        this.communityManager = communityManager;
    }

    @Required
    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }
}
