package com.jivesoftware.sbs.plugins.foo.action;

import java.util.Date;

import com.jivesoftware.community.Image;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveIterator;
import com.jivesoftware.community.action.util.AlwaysDisallowAnonymous;
import com.jivesoftware.community.impl.EmptyJiveIterator;
import com.jivesoftware.sbs.plugins.foo.Foo;

@AlwaysDisallowAnonymous
public class EditFooAction extends EditFooActionSupport {

    @Override
    public String input() {
        this.setTitle(foo.getTitle());
        this.setDescription(foo.getDescription());

        this.setTags(tagManager.getTagsAsString(foo));
        return super.input();
    }

    @Override
    public String execute() {
        populateFoo();
        foo.setModificationDate(new Date());

        JiveIterator<Image> images = EmptyJiveIterator.getInstance();
        if(getSessionKey() != null && getSession().get(getSessionKey()) != null) {
            Foo temp = (Foo) getSession().get(getSessionKey());
            images = temp.getImages();
        }

        fooManager.updateFoo(foo, images);

        this.cleanSession();
        tagActionUtil.saveTags(foo, validatedTags);
        getTagSetActionHelper().setTagSets(foo, getContentTagSets());
        if (isFooModerated()) {
            return SUCCESS_MODERATION;
        }

        return SUCCESS;
    }

    protected void populateFoo() {
        foo.setTitle(title);
        foo.setDescription(description);
    }

    @Override
    public JiveContainer getContainer() {
        return foo.getJiveContainer();
    }

    @Override
    public boolean isEdit() {
        return true;
    }
}
