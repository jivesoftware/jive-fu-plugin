package com.jivesoftware.sbs.plugins.foo.provider;

import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.base.User;
import com.jivesoftware.community.EntityDescriptor;
import com.jivesoftware.community.InterceptorManager;
import com.jivesoftware.community.NotFoundException;
import com.jivesoftware.community.comments.Comment;
import com.jivesoftware.community.comments.CommentContentResource;
import com.jivesoftware.community.comments.type.CommentAnonymousUserStrategy;
import com.jivesoftware.community.comments.type.CommentDelegatorStrategy;
import com.jivesoftware.community.comments.type.CommentableTypeInfoProviderSupport;
import com.jivesoftware.community.comments.type.UpgradeInfo;
import com.jivesoftware.sbs.plugins.foo.Foo;

public class FooCommentableTypeInfoProvider extends CommentableTypeInfoProviderSupport {
    private CommentAnonymousUserStrategy commentAnonymousUserStrategy;
    private CommentDelegatorStrategy commentDelegatorStrategy;

    @Override
    public int getCommentStatus(CommentContentResource commentTarget) throws NotFoundException {
        return commentTarget.getCommentStatus();
    }

    @Override
    public InterceptorManager getInterceptorManager(CommentContentResource commentTarget) throws NotFoundException {
        return ((Foo)commentTarget).getJiveContainer().getInterceptorManager();
    }

    @Override
    public EntityDescriptor getParentObject(CommentContentResource commentTarget) throws NotFoundException {
        return new EntityDescriptor(((Foo)commentTarget).getJiveContainer());
    }

    @Override
    public boolean isAuthorizedInBackChannel(CommentContentResource commentTarget, User user) {
        return false;
    }

    @Override
    public boolean isCommentAttachedToCommentTarget(EntityDescriptor commentTarget, EntityDescriptor comment) {
        return commentTarget.getID() == comment.getID();
    }

    @Override
    public boolean isCommentsEnabled() {
        return true;
    }

    @Override
    public boolean isDeleteable(CommentContentResource commentTarget, Comment comment) {
        return comment.getCommentContentResource() != null && commentTarget.getID() == comment.getCommentContentResource().getID();
    }

    @Override
    public boolean isInDraftState(CommentContentResource commentTarget) {
        return false;
    }

    @Override
    public boolean validateCommentOptions(CommentContentResource commentTarget, Comment comment, boolean isAnonymousUser) {
        return true;
    }

    @Override
    public CommentAnonymousUserStrategy getCommentAnonymousUserStrategy() {
        return commentAnonymousUserStrategy;
    }

    @Required
    public void setCommentAnonymousUserStrategy(CommentAnonymousUserStrategy commentAnonymousUserStrategy) {
        this.commentAnonymousUserStrategy = commentAnonymousUserStrategy;
    }

    @Override
    public CommentDelegatorStrategy getCommentDelegatorStrategy() {
        return commentDelegatorStrategy;
    }

    @Required
    public void setCommentDelegatorStrategy(CommentDelegatorStrategy commentDelegatorStrategy) {
        this.commentDelegatorStrategy = commentDelegatorStrategy;
    }

    @Override
    public UpgradeInfo getActivityUpgradeSQL() {
        return new UpgradeInfo(
                "JOIN jiveFoo foo ON foo.fooID = foo.objectID",
                "foo.containerType",
                "foo.containerID");
    }

}
