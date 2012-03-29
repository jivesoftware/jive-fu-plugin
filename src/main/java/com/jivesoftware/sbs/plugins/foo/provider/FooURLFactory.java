package com.jivesoftware.sbs.plugins.foo.provider;

import com.jivesoftware.community.Attachment;
import com.jivesoftware.community.AttachmentContentResource;
import com.jivesoftware.community.Image;
import com.jivesoftware.community.JiveGlobals;
import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.JiveObjectURLFactory;
import com.jivesoftware.community.comments.Comment;
import com.jivesoftware.community.comments.CommentContentResource;

public class FooURLFactory implements JiveObjectURLFactory {

    @Override
    public String createAttachmentContentResourceURL(AttachmentContentResource attachmentTarget, Attachment attachment) {
        return "";
    }

    @Override
    public String createCommentContentResourceURL(CommentContentResource commentTarget, Comment comment, boolean absolute) {
        StringBuilder url = new StringBuilder(this.createURL(commentTarget, absolute));

        url.append("#comment-");
        url.append(comment.getID());

        return url.toString();
    }

    @Override
    public String createEditFormURL(JiveObject jiveObject) {
        StringBuilder url = new StringBuilder(this.createURL(jiveObject, false));
        url.append("/edit");

        return url.toString();
    }

    @Override
    public String createImageURL(JiveObject imageTarget, Image image) {
        return null;
    }

    @Override
    public String createURL(JiveObject foo, boolean absolute) {
        StringBuilder url = new StringBuilder(32);

        if(absolute) {
            url.append(JiveGlobals.getDefaultBaseURL());
        }

        url.append("/foos/").append(foo.getID());
        return url.toString();
    }

    @Override
    public String createDownloadURL(JiveObject jiveObject, boolean absolute) {
        return "";
    }
}
