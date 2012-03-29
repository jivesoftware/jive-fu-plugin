package com.jivesoftware.sbs.plugins.foo.action;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.jivesoftware.base.User;
import com.jivesoftware.base.UserNotFoundException;
import com.jivesoftware.community.BanException;
import com.jivesoftware.community.BanManager;
import com.jivesoftware.community.Image;
import com.jivesoftware.community.JiveContentObject.Status;
import com.jivesoftware.community.JiveIterator;
import com.jivesoftware.community.RejectedException;
import com.jivesoftware.community.action.util.AlwaysDisallowAnonymous;
import com.jivesoftware.community.impl.EmptyJiveIterator;
import com.jivesoftware.community.web.JiveResourceResolver;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooBean;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;
import com.jivesoftware.util.DateUtils;

@AlwaysDisallowAnonymous
public class CreateFooAction extends EditFooActionSupport {
	private Logger log = Logger.getLogger(CreateFooAction.class);

    private String cancelURL;

    private long fooId;

    @Override
    public String execute() {
        FooBean bean = new FooBean();

        bean.setTitle(this.getTitle());
        bean.setDescription(this.getDescription());
        bean.setContainerID(this.getContainer().getID());
        bean.setContainerType(this.getContainer().getObjectType());
        bean.setUserID(this.getUser().getID());
        bean.setCreationDate(new Date());
        bean.setModificationDate(new Date());
        bean.setStatusID(Status.PUBLISHED.intValue());

        try {
            JiveIterator<Image> images = EmptyJiveIterator.getInstance();
            if(getSessionKey() != null && getSession().get(getSessionKey()) != null) {
                Foo temp = (Foo) getSession().get(getSessionKey());
                images = temp.getImages();
            }

            foo = fooManager.saveFoo(bean , images);
        } catch (RejectedException mje) {
            Throwable nested = mje.getCause();
            if (nested instanceof BanException) {
                BanException be = (BanException) nested;
                ArrayList<String> params = new ArrayList<String>(2);

                if (be.getBan().getBanType() == BanManager.TYPE_BAN_USER) {
                    // load the banned user
                    User bannedUser = null;
                    try {
                        bannedUser = getJiveContext().getUserManager().getUser(be.getBan().getBannedUserID());
                    } catch (UserNotFoundException unfe) {
                        log.error("Failed to load user with ID: " + be.getBan().getBannedUserID(), unfe);
                    }
                    params.add(bannedUser.getUsername());
                    if (be.getBan().getExpirationDate() != null) {
                        DateUtils dateUtils = new DateUtils(request, getUser());
                        params.add(dateUtils.getMediumFormatDate(be.getBan().getExpirationDate()));
                        addActionError(getText("post.banned_user.temporary.text", params));
                    } else {
                        addActionError(getText("post.banned_user.permanent.text", params));
                    }
                } else {
                    params.add(request.getRemoteAddr());
                    if (be.getBan().getExpirationDate() != null) {
                        DateUtils dateUtils = new DateUtils(request, getUser());
                        params.add(dateUtils.getMediumFormatDate(be.getBan().getExpirationDate()));
                        addActionError(getText("post.banned_ip.temporary.text", params));
                    } else {
                        addActionError(getText("post.banned_ip.permanent.text", params));
                    }
                }
            } else {
                addActionError(mje.getMessage());
            }
        }

        fooId = foo.getID();

        if (fooId > 0) {
            tagActionUtil.saveTags(foo, validatedTags);
            getTagSetActionHelper().setTagSets(foo, getContentTagSets());
        }

        this.cleanSession();
        if (isFooModerated()) {
            return SUCCESS_MODERATION;
        }

        return SUCCESS;
    }

    public String cancel() {
        this.cleanSession();

        return CANCEL;
    }
    protected String createCancelURL() {
        return JiveResourceResolver.getJiveObjectURL(getContainer()) + "?view=foo";
    }

    public String getCancelURL() {
        if (cancelURL == null) {
            cancelURL = createCancelURL();
        }
        return cancelURL;
    }

    public long getFooId() {
        return fooId;
    }

    public int getFooType() {
        return new FooObjectType().getID();
    }

    @Override
    public boolean isEdit() {
        return false;
    }
}
