package com.jivesoftware.sbs.plugins.foo.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.jivesoftware.community.JiveContentObject;
import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.action.ContentActionSupport;
import com.jivesoftware.community.moderation.JiveObjectModerator;
import com.jivesoftware.community.tags.TagActionUtil;
import com.jivesoftware.community.util.AttachmentPermHelper;
import com.jivesoftware.community.util.concurrent.LockUtil;
import com.jivesoftware.community.validation.InvalidPropertyException;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooBean;
import com.jivesoftware.sbs.plugins.foo.FooManager;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;
import com.jivesoftware.sbs.plugins.foo.impl.FooConverter;
import com.jivesoftware.sbs.plugins.foo.impl.FooImpl;
import com.opensymphony.xwork2.ActionContext;

public abstract class EditFooActionSupport extends ContentActionSupport {
    protected static final String SESSION_FOO_KEY = "jive.foo.message";
    public static final String SUCCESS_MODERATION = "success-moderation";

    protected String title;
    protected String description;

    protected String tags;
    protected Set<String> validatedTags = Collections.emptySet();
    protected Iterable<String> popularTags;

    protected FooConverter fooConverter;
    private boolean postedFromGUIEditor;
    protected Foo newFoo;

    protected Foo foo;

    protected FooManager fooManager;
    private JiveObjectModerator jiveObjectModerator;
    protected TagActionUtil tagActionUtil;

    @Override
    public void validate() {
        //TODO: validate input fields here

        if (!StringUtils.isBlank(tags)) {
            try {
                validatedTags = tagActionUtil.getValidTags(tags);
            }
            catch (InvalidPropertyException ipe) {
                switch (ipe.getError()) {
                    case length:
                        addFieldError("tags",
                                getText("doc.create.error.tag.length", new String[]{ipe.getProperty().toString()}));
                        break;
                    default:
                        addFieldError("tags",
                                getText("doc.create.error.tag.unknown", new String[]{ipe.getProperty().toString()}));
                        break;
                }
            }
        }
    }

    public String doImagePicker() {
        if (foo == null) {
            try {
                if (getSessionKey() != null && getSession().get(getSessionKey()) != null) {
                    foo = (Foo) getSession().get(getSessionKey());
                } else {
                    foo = getTemporaryFoo();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                addActionError(e.getMessage());
            }
        }

        getSession().put(getSessionKey(), foo);

        return "image-picker";
    }

    public String getSessionKey() {
        try {
            if (foo == null) {
                foo = getTemporaryFoo();
            }

            if (foo.getID() < 1) {
                return getFooSessionKey("");
            }

            return getFooSessionKey("" + foo.getID());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    protected Foo getTemporaryFoo() throws Exception {
        if (foo != null && getSession().containsKey(getFooSessionKey("" + foo.getID()))) {
            return (Foo) getSession().get(getFooSessionKey("" + foo.getID()));
        } else {
            return createTempFoo();
        }
    }

    private Foo createTempFoo() {
        FooBean bean = new FooBean();
        bean.setID(-1);
        bean.setUserID(this.getUser().getID());
        bean.setContainerID(getContainer().getID());
        bean.setContainerType(getContainer().getObjectType());

        return fooConverter.convert(bean);
    }

    public boolean hasPermissionsToUploadImages() {
        if(foo == null) {
            return AttachmentPermHelper.getCanCreateImageAttachment(getContainer(), FooObjectType.FOO_TYPE_ID);
        } else {
            return AttachmentPermHelper.getCanCreateImageAttachment(foo);
        }
    }

    protected String getFooSessionKey(String id) {
        return LockUtil.intern(SESSION_FOO_KEY + id);
    }

    protected void cleanSession() {
        ArrayList<String> removeables = new ArrayList<String>();
        Set keys = ActionContext.getContext().getSession().keySet();
        for (Object key1 : keys) {
            String key = (String) key1;
            if (key.startsWith(SESSION_FOO_KEY)) {
                removeables.add(key);
            }
        }

        for (String removeable : removeables) {
            getSession().remove(removeable);
        }
    }

    public String getLanguage() {
        return this.getLocale().getLanguage();
    }

    public void setNewFoo(Foo foo) {
        this.newFoo = foo;
    }

    public boolean isPostedFromGUIEditor() {
        return postedFromGUIEditor;
    }

    public void setPostedFromGUIEditor(boolean postedFromGUIEditor) {
        this.postedFromGUIEditor = postedFromGUIEditor;
    }

    public void setFooConverter(FooConverter fooConverter) {
        this.fooConverter = fooConverter;
    }

    public Foo getFoo() {
        return foo;
    }

    public void setFoo(Foo foo) {
        this.foo = foo;
    }

    public Iterable<String> getPopularTags() {
        if (popularTags == null) {
            popularTags = tagActionUtil.getPopularTags(getContainer(), 25);
        }
        return popularTags;
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


    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<Long> getObjectTagSetIDs(JiveObject object) {
        return getTagSetActionHelper().getObjectTagSetIDs(object);
    }
    public void setFooManager(FooManager fooManager) {
        this.fooManager = fooManager;
    }

    public void setTagActionUtil(TagActionUtil tagActionUtil) {
        this.tagActionUtil = tagActionUtil;
    }

    public boolean isFooModerated() {
        boolean moderationOn = jiveObjectModerator.isModerationEnabled(getContainer(), new FooImpl(), getUser());
        // Check to see if an interceptor has toggled the status to awaiting moderation
        boolean alreadyAwaitingModeration = getFoo() != null && getFoo().getStatus() == JiveContentObject.Status
                .AWAITING_MODERATION;

        return moderationOn || alreadyAwaitingModeration;
    }

    @Override
    public String input() {
        if (getJiveObjectModerator().isModerationEnabled(getContainer(), new FooImpl(), getUser())) {
            addActionMessage(getText("Please note, your foo will need to be approved by a moderator before it is posted."));
        }

        return super.input();
    }

    protected JiveObjectModerator getJiveObjectModerator() {
        return jiveObjectModerator;
    }

    public void setJiveObjectModerator(JiveObjectModerator jiveObjectModerator) {
        this.jiveObjectModerator = jiveObjectModerator;
    }

    public abstract boolean isEdit();
}
